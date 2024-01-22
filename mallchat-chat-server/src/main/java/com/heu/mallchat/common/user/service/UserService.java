package com.heu.mallchat.common.user.service;

import com.heu.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heu.mallchat.common.user.domain.vo.req.BlackReq;
import com.heu.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.heu.mallchat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

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

    List<BadgeResp> badges(Long uid);

    void wearingBadge(Long uid, Long itemId);

    void black(BlackReq req);
}
