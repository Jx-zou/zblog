package xyz.jxzou.zblog.service.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.jxzou.zblog.service.user.domain.entity.RolePermission;
import xyz.jxzou.zblog.service.user.mapper.PermissionMapper;
import xyz.jxzou.zblog.service.user.mapper.RoleMapper;
import xyz.jxzou.zblog.service.user.mapper.RolePermissionMapper;

@Slf4j
@SpringBootTest
public class RolePermissionTest {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    public void insert() {
        RolePermission rp = new RolePermission();
        Long rid = roleMapper.findIdByName("user");
        Long pid = permissionMapper.findIdByName("userinfo");
        rp.setRid(rid);
        rp.setPid(pid);
        int insert = rolePermissionMapper.insert(rp);
    }
}
