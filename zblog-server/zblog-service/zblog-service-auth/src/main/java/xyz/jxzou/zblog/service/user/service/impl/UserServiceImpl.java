package xyz.jxzou.zblog.service.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jxzou.zblog.service.user.domain.entity.User;
import xyz.jxzou.zblog.service.user.mapper.UserMapper;
import xyz.jxzou.zblog.service.user.service.UserService;

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
