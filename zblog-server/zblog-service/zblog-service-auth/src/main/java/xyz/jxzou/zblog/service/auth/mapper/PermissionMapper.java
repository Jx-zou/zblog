package xyz.jxzou.zblog.service.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.jxzou.zblog.service.auth.domain.entity.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("<script>select DISTINCT p.url from auth.permission p, auth.role r, auth.role_permission rp where p.id=rp.pid and r.id=rp.rid and r.id in " +
            "<foreach item='item' collection='roleIds' index='index' open='(' separator=',' close=')'>#{item}</foreach></script>")
    List<String> findUrlsByRoleIds(@Param("roleIds") List<Long> roleIds);

    @Select("select p.id from auth.permission p where p.name=#{name}")
    Long findIdByName(String name);
}
