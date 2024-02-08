package xyz.jxzou.zblog.service.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.jxzou.zblog.common.core.domain.pojo.CoreContent;
import xyz.jxzou.zblog.common.util.pojo.Page;
import xyz.jxzou.zblog.service.business.domain.entity.Article;
import xyz.jxzou.zblog.service.business.domain.vo.ArticleVo;
import xyz.jxzou.zblog.service.business.mapper.ArticleMapper;
import xyz.jxzou.zblog.service.business.service.ArticleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    @Resource
    private RedisTemplate<String, Object> businessRedisTemplate;

    @Override
    public Page<List<ArticleVo>> search(Integer size, Integer offset, String search) {
        Long total;
        String articleTotalName = CoreContent.REDIS_NAME_ARTICLE_TOTAL;
        if (Boolean.TRUE.equals(businessRedisTemplate.hasKey(articleTotalName))) {
            Object obj = businessRedisTemplate.opsForValue().get(articleTotalName);
            total = Objects.isNull(obj) ? 0L : new Long((Integer) obj);
        } else {
            total = articleMapper.selectCount(null);
            if (total > 0) {
                businessRedisTemplate.opsForValue().set(articleTotalName, total);
            }
        }

        //查询redis中是否有文章
        String articlePageName = CoreContent.REDIS_NAME_ARTICLE_PAGE;
        if (StringUtils.isBlank(search) && Boolean.TRUE.equals(businessRedisTemplate.hasKey(articlePageName))) {
            if (Boolean.TRUE.equals(businessRedisTemplate.opsForHash().hasKey(articlePageName, this.pageName(offset, size)))){
                return new Page<>(offset, size, total, (List<ArticleVo>) businessRedisTemplate.opsForHash().get(articlePageName, this.pageName(offset, size)));
            }
        }

        if (StringUtils.isBlank(search)) {
            search = "";
        }
        List<ArticleVo> articleVos = articleMapper.search(offset * size, size, search);
        if (CollectionUtils.isEmpty(articleVos)) {
            return null;
        }
        businessRedisTemplate.opsForHash().put(articlePageName, this.pageName(offset, size), articleVos);
        return new Page<>(offset, size, total, articleVos);
    }

    private String pageName(int page, int size) {
        return size + "_" + page;
    }
}
