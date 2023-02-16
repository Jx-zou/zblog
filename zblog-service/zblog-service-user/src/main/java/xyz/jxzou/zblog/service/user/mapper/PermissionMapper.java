package xyz.jxzou.zblog.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.jxzou.zblog.service.user.entity.Permission;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("select DISTINCT p.url from auth.permission p, auth.role r, auth.role_permission rp where p.id=rp.pid and r.id=rp.rid and r.id in #{roleIds}")
    List<Permission> findUrlByRoleIds(List<Long> roleIds);
}
