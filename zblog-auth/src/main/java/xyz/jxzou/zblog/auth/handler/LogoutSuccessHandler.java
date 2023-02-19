package xyz.jxzou.zblog.auth.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.auth.domain.dto.Payload;
import xyz.jxzou.zblog.auth.util.JwtTokenUtil;
import xyz.jxzou.zblog.common.core.pojo.RSAKeyPair;
import xyz.jxzou.zblog.common.core.util.RSAUtils;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.util.tool.ResponseUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    private final RSAKeyPair rsaKeyPair;
    private final JwtTokenUtil jwtTokenUtil;
    @Resource
    private RedisTemplate<String, Object> clientRedisTemplate;
    @Resource
    private RedisTemplate<String, Object> userRedisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            //获取token并验证
            String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            ServletResponseEnum.AUTH_TOKEN_ERROR.validateToken(authToken);

            //截取真实token
            final String token = authToken.split(" ")[1].trim();

            //解析token获得内容对象
            Payload payload = jwtTokenUtil.verify(token, rsaKeyPair.getPublicKey());

            String clientId = RSAUtils.base64Decrypt(payload.getCid(), rsaKeyPair.getPrivateKey().getEncoded());
            ServletResponseEnum.CLIENT_ID_ERROR.validateClientId(clientId);

            String userId = (String) clientRedisTemplate.opsForHash().get(clientId, payload.getUsername());
            ServletResponseEnum.AUTH_LOGOUT_ERROR.isBlank(userId);

            if (Boolean.FALSE.equals(userRedisTemplate.hasKey(userId))) {
                throw ServletResponseEnum.AUTH_LOGOUT_ERROR.newException();
            }

            userRedisTemplate.delete(userId);
            clientRedisTemplate.delete(clientId);
            ResponseUtils.writeJson(response, CommonResponseEnum.SUCCESS.getResult());
        } catch (Exception e) {
            log.error("注销失败");
            ResponseUtils.writeJson(response, ServletResponseEnum.AUTH_LOGOUT_ERROR.getResult());
        }
    }
}
