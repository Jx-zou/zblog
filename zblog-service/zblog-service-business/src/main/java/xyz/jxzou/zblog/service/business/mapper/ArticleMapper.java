package xyz.jxzou.zblog.service.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.jxzou.zblog.service.business.domain.entity.Article;
import xyz.jxzou.zblog.service.business.domain.vo.ArticleVo;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select a.*,u.nickname,u.profile,u.avatar from business.article a, auth.user u " +
            "where a.author=u.id " +
            "and a.id >= (select id from business.article order by id limit #{size} offset #{offset}) " +
            "and title like concat('%',#{search},'%') limit 10")
    List<ArticleVo> search(int offset, int size, String search);

    @Select("select a.id from business.article a where a.name=#{name}")
    Long findIdByName(String name);
}
