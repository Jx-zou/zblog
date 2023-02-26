package xyz.jxzou.zblog.service.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.jxzou.zblog.service.business.domain.entity.Article;
import xyz.jxzou.zblog.common.util.pojo.Page;
import xyz.jxzou.zblog.service.business.domain.vo.ArticleVo;

import java.util.List;

public interface ArticleService extends IService<Article> {

    Page<List<ArticleVo>> search(Integer size, Integer offset, String search);
}
