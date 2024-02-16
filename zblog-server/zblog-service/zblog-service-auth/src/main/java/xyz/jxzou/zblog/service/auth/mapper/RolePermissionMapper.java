package xyz.jxzou.zblog.service.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import xyz.jxzou.zblog.service.auth.domain.entity.RolePermission;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Insert("insert into auth.role_permission(rid, pid) values((select r.id from auth.role r where r.name=#{roleName}),(select p.id from auth.permission p where p.name=#{permissionName}))")
    void saveByName(String roleName, String permissionName);
}
