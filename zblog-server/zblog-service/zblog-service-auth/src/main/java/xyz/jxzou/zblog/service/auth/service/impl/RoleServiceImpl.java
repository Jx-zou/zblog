package xyz.jxzou.zblog.service.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.service.auth.mapper.RoleMapper;
import xyz.jxzou.zblog.service.auth.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
}
