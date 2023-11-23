package com.heu.mallchat.common.user.service.impl;

import com.heu.mallchat.common.user.dao.UserDao;
import com.heu.mallchat.common.user.domain.entity.User;
import com.heu.mallchat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Long register(User insertUser) {
        userDao.save(insertUser);
        //todo 用户注册的事件
        return insertUser.getId();
    }
}
