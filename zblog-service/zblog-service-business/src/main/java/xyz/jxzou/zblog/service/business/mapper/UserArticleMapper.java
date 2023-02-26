package xyz.jxzou.zblog.service.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.jxzou.zblog.service.business.domain.entity.UserArticle;

@Mapper
public interface UserArticleMapper extends BaseMapper<UserArticle> {
}
