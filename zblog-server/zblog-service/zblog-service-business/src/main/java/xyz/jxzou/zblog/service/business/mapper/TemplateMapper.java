package xyz.jxzou.zblog.service.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.jxzou.zblog.service.business.domain.entity.Template;

@Mapper
public interface TemplateMapper extends BaseMapper<Template> {

    @Select("select t.content from business.template t where t.type=#{type}")
    String findContentByType(Integer type);
}
