package com.heu.mallchat.common.user.service;

import com.heu.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.mallchat.common.user.domain.vo.resp.UserInfoResp;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/leeleezl">lizzzz</a>
 * @since 2023-11-20
 */
public interface UserService  {

    Long register(User insertUser);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);
}
