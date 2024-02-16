package xyz.jxzou.zblog.service.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.jxzou.zblog.service.auth.mapper.PermissionMapper;
import xyz.jxzou.zblog.service.auth.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper authorityMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }
}
