package xyz.jxzou.zblog.service.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.jxzou.zblog.service.business.domain.entity.Article;
import xyz.jxzou.zblog.service.business.domain.vo.ArticleVo;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select a.id, a.title, a.profile, a.url, a.create_time, u.nickname, u.profile uprofile, u.avatar " +
            "from business.article a, auth.user u " +
            "where a.author = u.id and title like concat('%',#{search},'%') limit #{size} offset #{offset}")
    List<ArticleVo> search(int offset, int size, String search);

    @Select("select a.id from business.article a where a.name=#{name}")
    Long findIdByName(String name);
}
