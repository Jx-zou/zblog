package xyz.jxzou.zblog.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import xyz.jxzou.zblog.auth.util.ResponseUtils;
import xyz.jxzou.zblog.core.pojo.enums.ServletResponseEnum;

@Slf4j
@Component
public class AuthenticationAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("权限不足，无法访问", denied);
        return ResponseUtils.jsonWriteAndFlushWith(exchange.getResponse(), ServletResponseEnum.PERMISSION_DENIED_ERROR.getResult());
    }
}
