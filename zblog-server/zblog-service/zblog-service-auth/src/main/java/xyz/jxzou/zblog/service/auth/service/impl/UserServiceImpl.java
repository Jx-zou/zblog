package xyz.jxzou.zblog.service.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jxzou.zblog.service.auth.domain.entity.User;
import xyz.jxzou.zblog.service.auth.mapper.UserMapper;
import xyz.jxzou.zblog.service.auth.service.UserService;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void change(String nickname, String desc, String userId) {
        userMapper.updateById(User.builder()
                .id(Long.parseLong(userId))
                .nickname(nickname)
                .profile(desc)
                .build());
    }
}
