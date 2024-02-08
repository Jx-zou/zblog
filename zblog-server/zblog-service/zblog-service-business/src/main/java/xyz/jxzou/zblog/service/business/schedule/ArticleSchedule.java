package xyz.jxzou.zblog.service.business.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.service.business.config.ArticleConfiguration;
import xyz.jxzou.zblog.service.business.domain.vo.ArticleVo;
import xyz.jxzou.zblog.service.business.mapper.ArticleMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ArticleSchedule {

    private final ArticleConfiguration articleConfiguration;
    private final ArticleMapper articleMapper;
    @Resource
    private RedisTemplate<String, Object> businessRedisTemplate;

    /**
     * 每小时执行一次, 查阅文章内容并存入redis中
     */
    @Scheduled(cron = "${article.cron.article-cache}")
    private void articleCache() {
        int size = articleConfiguration.getPageSize();
        Long count = articleMapper.selectCount(null);
        businessRedisTemplate.opsForValue().set(CoreContent.REDIS_NAME_ARTICLE_TOTAL, count);
        for (int offset = 0; offset < count / size; offset++) {
            List<ArticleVo> articleVos = articleMapper.search(offset, size, null);
            businessRedisTemplate.opsForHash().put(CoreContent.REDIS_NAME_ARTICLE_PAGE, obtainName(offset, size), articleVos);
        }
        businessRedisTemplate.expire(CoreContent.REDIS_NAME_ARTICLE_PAGE, 1L, TimeUnit.HOURS);
    }

    private String obtainName(int page, int size) {
        return size + "_" + page;
    }
}
