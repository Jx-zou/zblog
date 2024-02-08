package xyz.jxzou.zblog.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.jxzou.zblog.service.user.domain.entity.Role;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select r.id,r.name from auth.user u, auth.role r, auth.user_role ur where u.id=ur.uid and r.id=ur.rid and u.id=#{uid}")
    List<Role> findNameAndIdByUserId(Long uid);

    @Select("select r.id from auth.role r where r.name=#{name}")
    Long findIdByName(String name);
}
