package xyz.jxzou.zblog.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import xyz.jxzou.zblog.auth.util.ResponseUtils;
import xyz.jxzou.zblog.core.pojo.enums.CommonResponseEnum;

/**
 * The type CsrfDeniedHandler.
 *
 * @author Jx-zou
 */
@Slf4j
@Component
public class CsrfDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("登录错误", denied);
        return ResponseUtils.jsonWriteAndFlushWith(exchange.getResponse(), CommonResponseEnum.SERVER_ERROR.getResult());
    }
}
