package xyz.jxzou.zblog.auth.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.jxzou.zblog.common.core.domain.pojo.CommonContent;
import xyz.jxzou.zblog.common.core.domain.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.domain.pojo.SafetyManager;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.util.tool.ResponseUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientFilter extends OncePerRequestFilter {

    private final SafetyManager safetyManager;
    private final RedisTemplate<String, Object> clientRedisTemplate;

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws IOException {
        try {
            if (request == null || response == null || filterChain == null) {
                return;
            }
            if (request.getRequestURI().equals("/api/pkey")) {
                filterChain.doFilter(request, response);
                return;
            }
            String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isNotBlank(authToken) && authToken.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            //从header中获取cid并验证
            String cid = request.getHeader(CommonContent.HTTP_HEADER_CLIENT);
            String clientId = RSAUtils.base64Decrypt(cid, safetyManager.getRsaPrivetKey());
            ServletResponseEnum.CLIENT_ID_ERROR.validateClientId(clientId);
            //redis验证该clientId是否存在并重置过期时间
            if (clientRedisTemplate.opsForHash().hasKey(CommonContent.REDIS_PREFIX_CLIENT + clientId, CommonContent.REDIS_NAME_CLIENT_CREATE_TIME)) {
                clientRedisTemplate.expire(clientId, CommonContent.REDIS_CLIENT_EXPIRE, TimeUnit.SECONDS);
                request.setAttribute(CommonContent.HTTP_HEADER_CLIENT, clientId);
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            log.error("cid拦截", e);
            ResponseUtils.writeJson(response, CommonResponseEnum.ERROR.getResult());
        }
    }
}
