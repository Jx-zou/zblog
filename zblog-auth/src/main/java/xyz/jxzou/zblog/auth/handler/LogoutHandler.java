package xyz.jxzou.zblog.auth.handler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.auth.domain.pojo.dto.JwtUser;
import xyz.jxzou.zblog.common.core.domain.pojo.CommonContent;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.util.tool.ResponseUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    @Resource
    private RedisTemplate<String, Object> userRedisTemplate;

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            JwtUser principal = (JwtUser) authentication.getPrincipal();
            String userId = String.valueOf(principal.getId());
            if (StringUtils.isBlank(userId)) {
                ResponseUtils.writeJson(response, ServletResponseEnum.SERVLET_ERROR.getResult());
            }
            userRedisTemplate.delete(CommonContent.REDIS_PREFIX_USER + userId);
        } catch (Exception e) {
            log.error("注销失败");
            ResponseUtils.writeJson(response, ServletResponseEnum.SERVLET_ERROR.getResult());
        }
    }
}
