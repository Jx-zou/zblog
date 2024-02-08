package xyz.jxzou.zblog.service.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.service.user.mapper.RoleMapper;
import xyz.jxzou.zblog.service.user.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
}
