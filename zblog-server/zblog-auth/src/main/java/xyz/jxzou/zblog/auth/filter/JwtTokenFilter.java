package xyz.jxzou.zblog.auth.filter;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.jxzou.zblog.auth.domain.pojo.dto.JwtPayload;
import xyz.jxzou.zblog.auth.domain.pojo.dto.JwtUser;
import xyz.jxzou.zblog.auth.util.JwtTokenUtil;
import xyz.jxzou.zblog.common.core.config.JwtProp;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.common.core.domain.pojo.SafetyManager;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.util.tool.ResponseUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final SafetyManager safetyManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtProp jwtProp;
    @Resource
    private RedisTemplate<String, Object> userRedisTemplate;
    @Resource
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
            JwtPayload payload = jwtTokenUtil.verify(token, safetyManager.getRsaPublicKey());

            //获取username并验证
            String username = payload.getAccount();
            ServletResponseEnum.AUTH_LOGIN_USERNAME_ERROR.isBlank(username);

            //从clientRedis库获取userId并验证
            String userId = (String) clientRedisTemplate.opsForHash().get(CoreContent.REDIS_PREFIX_CLIENT + clientId, username);
            ServletResponseEnum.AUTH_FAILED_ERROR.isBlank(userId);

            //据userId从userRedis库获取JwtUser对象,获取其用户信息
            JwtUser jwtUser = JSON.parseObject(JSON.toJSONString(userRedisTemplate.opsForValue().get(CoreContent.REDIS_PREFIX_USER + userId)), JwtUser.class);
            ServletResponseEnum.AUTH_FAILED_ERROR.isNull(jwtUser);

            //重置user的过期时间
            userRedisTemplate.expire(CoreContent.REDIS_PREFIX_USER + userId, jwtProp.getAccessExpire(), TimeUnit.SECONDS);

            Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
            ServletResponseEnum.PERMISSION_DENIED_ERROR.isEmpty(authorities);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("认证失败");
            ResponseUtils.writeJson(response, CommonResponseEnum.ERROR.getResult());
        }
    }
}
