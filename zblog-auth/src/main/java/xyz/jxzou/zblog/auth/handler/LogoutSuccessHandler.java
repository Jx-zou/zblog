package xyz.jxzou.zblog.auth.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.auth.pojo.AuthContent;
import xyz.jxzou.zblog.common.core.util.ResponseUtils;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.redis.processor.DynamicRedisTemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    private final RedisTemplate<String, Object> clientRedisTemplate;

    @Autowired
    public LogoutSuccessHandler(DynamicRedisTemplateProcessor processor) {
        this.clientRedisTemplate = processor.getRedisTemplate("client");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String clientId = request.getHeader(AuthContent.CLIENT_HEADER_NAME);
        if (clientId == null || clientId.length() != 24) {
            ResponseUtils.writeJson(response, CommonResponseEnum.SERVER_ERROR.getResult());
            return;
        }
        clientRedisTemplate.delete(clientId);
        ResponseUtils.writeJson(response, CommonResponseEnum.SUCCESS.getResult());
    }
}
