package xyz.jxzou.zblog.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.common.util.tool.ResponseUtils;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("权限不足，无法访问", accessDeniedException);
        ResponseUtils.writeJson(response, ServletResponseEnum.PERMISSION_DENIED_ERROR.getResult());
    }
}
