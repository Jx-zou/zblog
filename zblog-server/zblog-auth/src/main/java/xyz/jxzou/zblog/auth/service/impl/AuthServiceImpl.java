package xyz.jxzou.zblog.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.jxzou.zblog.auth.domain.pojo.dto.JwtPayload;
import xyz.jxzou.zblog.auth.domain.pojo.dto.JwtUser;
import xyz.jxzou.zblog.auth.domain.pojo.vo.LUserVo;
import xyz.jxzou.zblog.auth.domain.pojo.vo.RUserVo;
import xyz.jxzou.zblog.auth.service.AuthService;
import xyz.jxzou.zblog.auth.util.JwtTokenUtils;
import xyz.jxzou.zblog.common.core.config.CaptchaProp;
import xyz.jxzou.zblog.common.core.config.JwtProp;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.common.core.domain.pojo.SafetyManager;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.common.exception.enums.BusinessResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.exception.model.exception.ServletException;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.common.util.tool.Maps;
import xyz.jxzou.zblog.service.user.domain.entity.Role;
import xyz.jxzou.zblog.service.user.domain.entity.User;
import xyz.jxzou.zblog.service.user.domain.entity.UserRole;
import xyz.jxzou.zblog.auth.domain.pojo.vo.UserinfoVo;
import xyz.jxzou.zblog.service.user.mapper.PermissionMapper;
import xyz.jxzou.zblog.service.user.mapper.RoleMapper;
import xyz.jxzou.zblog.service.user.mapper.UserMapper;
import xyz.jxzou.zblog.service.user.mapper.UserRoleMapper;
import xyz.jxzou.zblog.service.user.service.UserService;
import xyz.jxzou.zblog.upload.domain.vo.FileVo;
import xyz.jxzou.zblog.upload.service.UploadService;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProp jwtProp;
    private final CaptchaProp captchaProp;
    private final SafetyManager safetyManager;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final UploadService uploadService;

    @Resource
    private RedisTemplate<String, Object> userRedisTemplate;
    @Resource
    private RedisTemplate<String, Object> clientRedisTemplate;
    @Resource
    private RedisTemplate<String, Object> captchaRedisTemplate;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userMapper.findByAccountOrPhoneOrMail(account);
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        List<Role> roles = roleMapper.findNameAndIdByUserId(user.getId());
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<String> urls = permissionMapper.findUrlsByRoleIds(roleIds);

        return JwtUser.create(user, roles, urls);
    }

    @Override
    public String createPublicKey() {
        return safetyManager.getBase64RsaPublicKey();
    }

    @Override
    public Map<String, String> login(LUserVo authUser) throws Exception {
        String account = authUser.getUsername();
        String secret = authUser.getPassword();

        RSAPrivateKey privateKey = safetyManager.getRsaPrivetKey();
        account = RSAUtils.base64Decrypt(account, privateKey);
        secret = RSAUtils.base64Decrypt(secret, privateKey);

        Authentication authenticate = authenticationConfiguration.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(account, secret));

        JwtUser jwtUser = (JwtUser) authenticate.getPrincipal();
        String username = jwtUser.getUsername();
        //判断是否已登录,如已登录,则拒绝登录操作
        if (clientRedisTemplate.opsForHash().hasKey(redisClientName, username)) {
            throw ServletResponseEnum.AUTH_LOGIN_REPEAT_ERROR.newException();
        }
        String userId = jwtUser.getId().toString();
        if (Boolean.TRUE.equals(userRedisTemplate.hasKey(userId))) {
            throw ServletResponseEnum.AUTH_LOGIN_REPEAT_ERROR.newException();
        }
        //存入userId
        clientRedisTemplate.opsForHash().put(redisClientName, username, userId);
        //生成token
        String userToken = jwtProp.getAccessPrefix() + jwtTokenUtil.generate(getPayload(jwtUser.getUsername(), RSAUtils.base64Encrypt(clientId, safetyManager.getRsaPublicKey())), privateKey);
        //将user存入redis实现登录操作
        String redisUserName = CoreContent.REDIS_PREFIX_USER + userId;
        userRedisTemplate.opsForValue().set(redisUserName, jwtUser);
        userRedisTemplate.expire(redisUserName, jwtProp.getAccessExpire(), TimeUnit.SECONDS);

        return new Maps<String, String>()
                .put(CoreContent.HTTP_HEADER_ACCESS_TOKEN, userToken)
                .put("nickname", jwtUser.getNickname())
                .put("desc", jwtUser.getDesc())
                .put("avatar", jwtUser.getAvatar())
                .hashMap();
    }

    @Override
    public ResponseResult<Void> registry(RUserVo rUserVo, String clientId) throws Exception {
        RSAPrivateKey rsaprivateKey = safetyManager.getRsaPrivetKey();
        //解密code
        Integer mailCaptchaSize = captchaProp.getParam("mail").getSize();
        String code = RSAUtils.base64Decrypt(rUserVo.getCode(), rsaprivateKey);
        BusinessResponseEnum.CAPTCHA_ERROR.validateCaptcha(code, mailCaptchaSize);
        //获取redis中的code
        String redisCode = (String) captchaRedisTemplate.opsForValue().get(CoreContent.REDIS_PREFIX_CAPTCHA + clientId);
        BusinessResponseEnum.CAPTCHA_EXPIRE_ERROR.validateCaptcha(redisCode, mailCaptchaSize);

        if (!code.equals(redisCode)) {
            return BusinessResponseEnum.CAPTCHA_ERROR.getResult();
        }
        //校验mail
        String mail = RSAUtils.base64Decrypt(rUserVo.getMail(), rsaprivateKey);
        BusinessResponseEnum.MAIL_BOX_ERROR.validateMail(mail);
        //校验password
        String password = RSAUtils.base64Decrypt(rUserVo.getPassword(), rsaprivateKey);
        BusinessResponseEnum.REGISTRY_PASSWORD_ERROR.validatePassword(password);
        //校验nickname
        String nickname = RSAUtils.base64Decrypt(rUserVo.getNickname(), rsaprivateKey);
        BusinessResponseEnum.REGISTRY_NICKNAME_ERROR.validateNickname(nickname);
        //读取个人简介
        String desc = RSAUtils.base64Decrypt(rUserVo.getDesc(), rsaprivateKey);
        BusinessResponseEnum.REGISTRY_DESCRIPTION_ERROR.validateDescription(desc);

        User user = User.builder().nickname(nickname).mail(mail).secret(passwordEncoder.encode(password)).profile(desc).build();

        //注册
        if (userMapper.hasUser(rUserVo.getMail()) > 0) {
            return BusinessResponseEnum.REGISTRY_ERROR.getResult();
        }

        userMapper.insert(user);
        Long userId = userMapper.findIdByMail(user.getMail());
        UserRole userRole = new UserRole();
        userRole.setUid(userId);

        //授权 基本权限user
        Long roleId = roleMapper.findIdByName(CoreContent.BASIC_ROLE);
        if (roleId == null) {
            return BusinessResponseEnum.REGISTRY_AUTHORIZATION_ERROR.getResult();
        }
        userRole.setRid(roleId);
        userRoleMapper.insert(userRole);

        return BusinessResponseEnum.REGISTRY_SUCCESS.getResult();
    }

    @Override
    public void changeUserinfo(UserinfoVo userinfoVo, String userId) {
        userService.change(userinfoVo.getNickname(), userinfoVo.getDesc(), userId);
        JwtUser jwtUser = JSON.parseObject(JSON.toJSONString(userRedisTemplate.opsForValue().get(CoreContent.REDIS_PREFIX_USER + userId)), JwtUser.class);
        jwtUser.setNickname(userinfoVo.getNickname());
        jwtUser.setDesc(userinfoVo.getDesc());
        userRedisTemplate.opsForValue().set(CoreContent.REDIS_PREFIX_USER + userId, jwtUser, jwtProp.getAccessExpire(), TimeUnit.SECONDS);
    }

    @Override
    public FileVo uploadAvatar(MultipartFile file, String userId) throws IOException {
        FileVo fileVo = uploadService.uploadPhoto(new MultipartFile[]{file}).get(0);
        userMapper.updateById(User.builder().id(Long.parseLong(userId)).avatar(fileVo.getUrl()).build());
        JwtUser jwtUser = JSON.parseObject(JSON.toJSONString(userRedisTemplate.opsForValue().get(CoreContent.REDIS_PREFIX_USER + userId)), JwtUser.class);
        jwtUser.setAvatar(fileVo.getUrl());
        userRedisTemplate.opsForValue().set(CoreContent.REDIS_PREFIX_USER + userId, jwtUser, jwtProp.getAccessExpire(), TimeUnit.SECONDS);
        return fileVo;
    }

    @Override
    public void logout(String userId) throws ServletException {
        if (StringUtils.isBlank(userId)) {
            throw  ServletResponseEnum.SERVLET_ERROR.newException();
        }
        userRedisTemplate.delete(CoreContent.REDIS_PREFIX_USER + userId);
    }

    private JwtPayload getPayload(String account, String cid) {
        LocalDateTime now = LocalDateTime.now();
        return JwtPayload.builder()
                .sub(jwtProp.getAccessSub())
                .iss(jwtProp.getAccessIss())
                .iat(now.toEpochSecond(ZoneOffset.of("+8")))
                .jti(UUID.randomUUID().toString())
                .cid(cid)
                .account(account)
                .build();
    }
}
