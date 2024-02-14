package xyz.jxzou.zblog.auth.util;

import com.alibaba.fastjson2.JSON;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import xyz.jxzou.zblog.util.model.Result;

/**
 * The type ResponseUtils.
 *
 * @author Jx-zou
 */
public class ResponseUtils {

    public static Mono<Void> jsonWriteAndFlushWith(ServerHttpResponse response, Result result){
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Mono<Mono<DataBuffer>> mono = Mono.just(ByteBufMono.just(response.bufferFactory().wrap(JSON.toJSONString(result).getBytes())));
        return response.writeAndFlushWith(mono);
    }

}
