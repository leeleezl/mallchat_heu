package com.heu.mallchat.common.user.service.impl;

import com.heu.mallchat.common.user.dao.RoleDao;
import com.heu.mallchat.common.user.domain.enums.RoleEnum;
import com.heu.mallchat.common.user.service.IRoleService;
import com.heu.mallchat.common.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        Set<Long> roleSet = userCache.getRoleSetByUid(uid);
        return roleSet.contains(roleEnum.getId()) || isAdmin(roleSet);

    }

    private boolean isAdmin(Set<Long> roleSet) {
        return roleSet.contains(RoleEnum.ADMIN.getId());
    }
}
