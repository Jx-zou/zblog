package xyz.jxzou.zblog.service.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.jxzou.zblog.common.exception.enums.BusinessResponseEnum;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;
import xyz.jxzou.zblog.common.util.pojo.Page;
import xyz.jxzou.zblog.service.business.domain.vo.ArticleVo;
import xyz.jxzou.zblog.service.business.service.ArticleService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("search/{size}/{offset}")
    public ResponseResult<Page<List<ArticleVo>>> search(@PathVariable @NotNull Integer size, @PathVariable @NotNull Integer offset, @RequestBody(required = false) String search) {
        return BusinessResponseEnum.ARTICLE_SUCCESS.getResult(articleService.search(size, offset, search));
    }
}
