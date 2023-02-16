package xyz.jxzou.zblog.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import xyz.jxzou.zblog.auth.pojo.AuthContent;
import xyz.jxzou.zblog.auth.vo.JwtUser;
import xyz.jxzou.zblog.common.core.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.exception.model.exception.BaseException;
import xyz.jxzou.zblog.redis.processor.DynamicRedisTemplateProcessor;
import xyz.jxzou.zblog.service.user.entity.Permission;
import xyz.jxzou.zblog.service.user.entity.Role;
import xyz.jxzou.zblog.service.user.entity.User;
import xyz.jxzou.zblog.service.user.mapper.PermissionMapper;
import xyz.jxzou.zblog.service.user.mapper.RoleMapper;
import xyz.jxzou.zblog.service.user.mapper.UserMapper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The type Auth service.
 */
@Slf4j
@Service
@Transactional
public class AuthService implements UserDetailsService {

    private final RedisTemplate<String, Object> userRedisTemplate;
    private final RedisTemplate<String, Object> clientRedisTemplate;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    /**
     * Instantiates a new Auth service.
     *
     * @param processor        the processor
     * @param userMapper       the user mapper
     * @param roleMapper       the role mapper
     * @param permissionMapper the permission mapper
     */
    @Autowired
    public AuthService(DynamicRedisTemplateProcessor processor, UserMapper userMapper, RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.userRedisTemplate = processor.getRedisTemplate(AuthContent.USER_DATABASE_NAME);
        this.clientRedisTemplate = processor.getRedisTemplate(AuthContent.CLIENT_DATABASE_NAME);
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

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
     * @param clientId the client id
     * @return the string
     * @throws BaseException the base exception
     */
    public String createPublicKey(String clientId) throws BaseException {
        ServletResponseEnum.CLIENT_ID_ERROR.isNull(clientId);
        clientId = Arrays.toString(Base64Utils.decodeFromString(clientId));
        if (clientId.length() != 24) {
            throw ServletResponseEnum.CLIENT_ID_ERROR.newException();
        }
        RSAKeyPair rsaKeyPair = RSAUtils.generateRSAKeyPair();
        clientRedisTemplate.opsForHash().put(clientId, "privateKey", rsaKeyPair.getBase64PrivateKey());
        clientRedisTemplate.expire(clientId, AuthContent.CLIENT_DATABASE_EXPIRE, TimeUnit.HOURS);
        return rsaKeyPair.getBase64PublicKey();
    }
}
