package xyz.jxzou.zblog.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.common.util.tool.ResponseUtils;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtils.writeJson(response, ServletResponseEnum.AUTH_LOGIN_SUCCESS.getResult());
    }
}
