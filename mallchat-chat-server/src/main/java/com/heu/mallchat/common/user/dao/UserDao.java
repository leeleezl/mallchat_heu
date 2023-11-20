package com.heu.mallchat.common.user.dao;

import com.heu.mallchat.common.user.domain.entity.User;
import com.heu.mallchat.common.user.mapper.UserMapper;
import com.heu.mallchat.common.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/leeleezl">lizzzz</a>
 * @since 2023-11-20
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

}
