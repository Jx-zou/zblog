package xyz.jxzou.zblog.auth.handler;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.common.util.tool.ResponseUtils;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Login failed handler.
 */
@Slf4j
@Component
public class LoginFailedHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("登录错误", exception);
        ResponseUtils.writeJson(response, JSON.toJSON(ServletResponseEnum.AUTH_LOGIN_ERROR.getResult()));
    }
}
