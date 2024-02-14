package xyz.jxzou.zblog.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import xyz.jxzou.zblog.auth.util.ResponseUtils;
import xyz.jxzou.zblog.core.pojo.enums.ServletResponseEnum;

/**
 * The type LogoutHandler.
 *
 * @author Jx-zou
 */
@Slf4j
@Component
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return ResponseUtils.jsonWriteAndFlushWith(exchange.getExchange().getResponse(), ServletResponseEnum.AUTH_LOGOUT_ERROR.getResult());
    }
}
