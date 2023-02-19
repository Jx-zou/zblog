package xyz.jxzou.zblog.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import xyz.jxzou.zblog.service.user.entity.User;
import xyz.jxzou.zblog.service.user.mapper.UserMapper;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void query() {
        User user = userMapper.findByAccount("123");
        System.out.println(user);
    }

    @Test
    public void insert() {
        User user = User.builder()
                .secret(passwordEncoder.encode("jx_zou8023"))
                .nickname("Jx")
                .mail("jx.zou@foxmail.com")
                .avatar("https://www.jxzou.com/images/head.png")
                .profile("Hi!")
                .build();

        if(userMapper.insert(user) > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }
}
