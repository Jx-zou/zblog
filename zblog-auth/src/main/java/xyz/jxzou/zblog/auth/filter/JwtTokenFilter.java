package xyz.jxzou.zblog.auth.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.jxzou.zblog.auth.config.AuthProperties;
import xyz.jxzou.zblog.auth.domain.dto.Payload;
import xyz.jxzou.zblog.auth.domain.vo.JwtUser;
import xyz.jxzou.zblog.auth.util.JwtTokenUtil;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.core.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.util.RSAUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final RSAKeyPair rsaKeyPair;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthProperties authProperties;
    @Resource(name = "userRedisTemplate")
    private RedisTemplate<String, Object> userRedisTemplate;
    @Resource(name = "clientRedisTemplate")
    private RedisTemplate<String, Object> clientRedisTemplate;

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request == null || response == null || filterChain == null) {
                return;
            }
            //获取token并验证
            String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isEmpty(authToken) || !authToken.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            //截取真实token
            final String token = authToken.split(" ")[1].trim();

            //解析token获得内容对象
            Payload payload = jwtTokenUtil.verify(token, rsaKeyPair.getPublicKey());

            //获取cid
            String cid = payload.getCid();
            ServletResponseEnum.CLIENT_ID_ERROR.isBlank(cid);

            //解密cid获得clientId并验证
            RSAPrivateKey privateKey = rsaKeyPair.getPrivateKey();
            String clientId = RSAUtils.base64Decrypt(cid, privateKey.getEncoded());
            ServletResponseEnum.CLIENT_ID_ERROR.validateClientId(clientId);

            //获取username并验证
            String username = payload.getUsername();
            ServletResponseEnum.AUTH_LOGIN_USERNAME_ERROR.isBlank(username);

            //从clientRedis库获取userId并验证
            String userId = (String) clientRedisTemplate.opsForHash().get(clientId, username);
            ServletResponseEnum.AUTH_FAILED_ERROR.isBlank(userId);

            //据userId从userRedis库获取JwtUser对象,获取其用户信息
            JwtUser jwtUser = (JwtUser) userRedisTemplate.opsForValue().get(userId);
            ServletResponseEnum.AUTH_FAILED_ERROR.isNull(jwtUser);

            //重置user的过期时间
            userRedisTemplate.expire(userId, authProperties.getJwt().getExpire(), TimeUnit.HOURS);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("认证失败");
            filterChain.doFilter(request, response);
        }
    }
}
