package xyz.jxzou.zblog.service.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.service.user.mapper.PermissionMapper;
import xyz.jxzou.zblog.service.user.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper authorityMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }
}
