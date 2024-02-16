package xyz.jxzou.zblog.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import xyz.jxzou.zblog.auth.util.ResponseUtils;
import xyz.jxzou.zblog.core.pojo.enums.ServletResponseEnum;

@Slf4j
@Component
public class LoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.info("登录");
        return ResponseUtils.jsonWriteAndFlushWith(exchange.getExchange().getResponse(), ServletResponseEnum.AUTH_LOGIN_SUCCESS.getResult());
    }
}
