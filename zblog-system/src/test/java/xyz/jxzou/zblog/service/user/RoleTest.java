package xyz.jxzou.zblog.service.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.jxzou.zblog.service.user.domain.entity.Role;
import xyz.jxzou.zblog.service.user.mapper.RoleMapper;

@Slf4j
@SpringBootTest
public class RoleTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void insert() {
        Role role = new Role();
        role.setName("user");
        role.setProfile("基本用户");
        if (roleMapper.insert(role) > 0) {
            log.info("添加成功");
        } else {
            log.info("添加失败");
        }
    }
}
