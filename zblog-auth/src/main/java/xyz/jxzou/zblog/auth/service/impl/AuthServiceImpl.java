package xyz.jxzou.zblog.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import xyz.jxzou.zblog.auth.config.AuthProperties;
import xyz.jxzou.zblog.auth.domain.dto.Payload;
import xyz.jxzou.zblog.auth.domain.vo.JwtUser;
import xyz.jxzou.zblog.auth.domain.vo.RUserVo;
import xyz.jxzou.zblog.auth.service.AuthService;
import xyz.jxzou.zblog.auth.util.JwtTokenUtil;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.common.util.tool.Maps;
import xyz.jxzou.zblog.common.util.tool.NanoIDUtils;
import xyz.jxzou.zblog.common.exception.enums.BusinessResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.exception.model.exception.ServletException;
import xyz.jxzou.zblog.common.core.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.pojo.ZBlogContent;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.service.user.entity.Permission;
import xyz.jxzou.zblog.service.user.entity.Role;
import xyz.jxzou.zblog.service.user.entity.User;
import xyz.jxzou.zblog.service.user.entity.UserRole;
import xyz.jxzou.zblog.service.user.mapper.PermissionMapper;
import xyz.jxzou.zblog.service.user.mapper.RoleMapper;
import xyz.jxzou.zblog.service.user.mapper.UserMapper;
import xyz.jxzou.zblog.service.user.mapper.UserRoleMapper;

import javax.annotation.Resource;
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

    private final RSAKeyPair rsaKeyPair;
    private final AuthProperties authProperties;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;

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
        List<Permission> permissions = permissionMapper.findUrlByRoleIds(roleIds);
        return JwtUser.create(user, roles, permissions);
    }

    /**
     * Create public key string.
     *
     * @param cid the cid
     * @return the string
     * @throws ServletException the servlet exception
     */
    @Override
    public Map<String, String> createPublicKey(String cid) throws Exception {
        cid = new String(Base64Utils.decodeFromString(cid));
        ServletResponseEnum.CLIENT_ID_ERROR.validateClientId(cid);
        String clientId = NanoIDUtils.alphanumericId();
        clientRedisTemplate.opsForHash().put(clientId, ZBlogContent.CLIENT_REDIS_CREATE_TIME_NAME, LocalDateTime.now());
        clientRedisTemplate.expire(clientId, authProperties.getClient().getExpire(), TimeUnit.HOURS);
        return new Maps<String, String>()
                .put("clientId", RSAUtils.base64Encrypt(clientId, rsaKeyPair.getPublicKey().getEncoded()))
                .put("publicKey", rsaKeyPair.getBase64PublicKey())
                .hashMap();
    }

    /**
     * Login string.
     *
     * @param authUser 认证封装对象
     * @param cid   cid
     * @return the string
     * @throws Exception the exception
     */
    @Override
    public String login(JwtUser authUser, String cid) throws Exception {
        String account = authUser.getUsername();
        String secret = authUser.getPassword();

        RSAPrivateKey privateKey = rsaKeyPair.getPrivateKey();
        account = RSAUtils.base64Decrypt(account, privateKey.getEncoded());
        secret = RSAUtils.base64Decrypt(secret, privateKey.getEncoded());

        Authentication authenticate = authenticationConfiguration.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(account, secret));

        JwtUser jwtUser = (JwtUser) authenticate.getPrincipal();

        String clientId = RSAUtils.base64Decrypt(cid, privateKey.getEncoded());
        String username = jwtUser.getUsername();
        //判断是否已登录,如已登录,则拒绝登录操作
        if (clientRedisTemplate.opsForHash().hasKey(clientId, username)) {
            throw ServletResponseEnum.AUTH_LOGIN_REPEAT_ERROR.newException();
        }
        String userId = jwtUser.getId().toString();
        if (Boolean.TRUE.equals(userRedisTemplate.hasKey(userId))) {
            throw ServletResponseEnum.AUTH_LOGIN_REPEAT_ERROR.newException();
        }
        //存入userId
        clientRedisTemplate.opsForHash().put(clientId, username, userId);
        //重置client过期时间
        clientRedisTemplate.expire(clientId, authProperties.getClient().getExpire(), TimeUnit.HOURS);
        String userToken = authProperties.getJwt().getToken().getHeader() + jwtTokenUtil.generate(getPayload(jwtUser, cid), privateKey);
        //将user存入redis实现登录操作
        userRedisTemplate.opsForValue().set(userId, jwtUser);
        userRedisTemplate.expire(userId, authProperties.getJwt().getExpire(), TimeUnit.HOURS);

        return userToken;
    }

    /**
     * Registry.
     *
     * @param rUserVo      rUserVo
     * @param cid       cid
     */
    @Override
    public ResponseResult<Void> registry(RUserVo rUserVo, String cid) throws Exception {
        //校验cid
        CommonResponseEnum.STRING_ERROR.isBlank(cid);

        byte[] privateKey = rsaKeyPair.getPrivateKey().getEncoded();
        //解密cid获得clientId
        String clientId = RSAUtils.base64Decrypt(cid, privateKey);
        ServletResponseEnum.CLIENT_ID_ERROR.validateClientId(clientId);
        //解密code
        String code = RSAUtils.base64Decrypt(rUserVo.getCode(), privateKey);
        BusinessResponseEnum.CAPTCHA_ERROR.validateCaptcha(code, 6);
        //获取redis中的code
        String redisCode = (String) captchaRedisTemplate.opsForValue().get(ZBlogContent.CAPTCHA_REDIS_PREFIX + clientId);
        BusinessResponseEnum.CAPTCHA_ERROR.validateCaptcha(redisCode, 6);

        if (!code.equals(redisCode)) {
            return BusinessResponseEnum.CAPTCHA_ERROR.getResult();
        }
        //校验mail
        String mail = RSAUtils.base64Decrypt(rUserVo.getMail(), privateKey);
        BusinessResponseEnum.MAIL_BOX_ERROR.validateMail(mail);
        //校验password
        String password = RSAUtils.base64Decrypt(rUserVo.getPassword(), privateKey);
        BusinessResponseEnum.REGISTRY_PASSWORD_ERROR.validatePassword(password);
        //读取个人简介
        String desc = new String(Base64Utils.decodeFromString(rUserVo.getDesc()));

        User user = User.builder()
                .nickname(rUserVo.getNickname())
                .mail(mail)
                .secret(password)
                .profile(desc)
                .build();

        //注册
        if (userMapper.hasUser(rUserVo.getMail()) > 0) {
            return BusinessResponseEnum.REGISTRY_ERROR.getResult();
        }

        userMapper.insert(user);
        Long userId = userMapper.findIdByMail(rUserVo.getMail());
        UserRole userRole = new UserRole();
        userRole.setUid(userId);

        //授权基本权限user
        Long roleId = roleMapper.findIdByName(ZBlogContent.DEFAULT_USER_ROLE_NAME);
        if (roleId == null) {
            return BusinessResponseEnum.REGISTRY_AUTHORIZATION_ERROR.getResult();
        }
        userRole.setRid(roleId);
        userRoleMapper.insert(userRole);

        return BusinessResponseEnum.REGISTRY_SUCCESS.getResult();
    }

    private Payload getPayload(JwtUser jwtUser, String cid) {
        LocalDateTime now = LocalDateTime.now();
        return Payload.builder()
                .sub(authProperties.getJwt().getToken().getSub())
                .iss(authProperties.getJwt().getToken().getIss())
                .iat(now.toEpochSecond(ZoneOffset.of("+8")))
                .exp(now.plusHours(3).toEpochSecond(ZoneOffset.of("+8")))
                .jti(UUID.randomUUID().toString())
                .cid(cid)
                .username(jwtUser.getUsername())
                .mail(jwtUser.getMail())
                .nickname(jwtUser.getNickname())
                .desc(jwtUser.getDesc())
                .roles(jwtUser.getRoles())
                .build();
    }
}
