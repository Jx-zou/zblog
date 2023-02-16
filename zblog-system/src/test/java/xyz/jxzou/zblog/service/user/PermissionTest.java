package xyz.jxzou.zblog.service.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.jxzou.zblog.service.user.entity.Permission;
import xyz.jxzou.zblog.service.user.mapper.PermissionMapper;

@Slf4j
@SpringBootTest
public class PermissionTest {

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    public void insert() {
        Permission permission = new Permission();
        permission.setName("data");
        permission.setUrl("/data");
        permission.setProfile("数据");
        if (permissionMapper.insert(permission) > 0) {
            log.info("添加成功");
        } else {
            log.info("添加失败");
        }
    }
}
