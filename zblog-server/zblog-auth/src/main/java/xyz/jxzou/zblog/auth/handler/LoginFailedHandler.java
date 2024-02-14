package xyz.jxzou.zblog.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import xyz.jxzou.zblog.auth.util.ResponseUtils;
import xyz.jxzou.zblog.core.pojo.enums.ServletResponseEnum;

/**
 * The type Login failed handler.
 */
@Slf4j
@Component
public class LoginFailedHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange exchange, AuthenticationException exception) {
        log.error("登录错误", exception);
        return ResponseUtils.jsonWriteAndFlushWith(exchange.getExchange().getResponse(), ServletResponseEnum.AUTH_LOGIN_ERROR.getResult());
    }
}
