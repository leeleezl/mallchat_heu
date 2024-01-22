package com.heu.mallchat.common.user.service.impl;

import cn.hutool.log.Log;
import com.heu.mallchat.common.common.event.UserBlackEvent;
import com.heu.mallchat.common.common.event.UserRegisterEvent;
import com.heu.mallchat.common.common.exception.BusinessException;
import com.heu.mallchat.common.common.utils.AssertUtil;
import com.heu.mallchat.common.user.dao.BlackDao;
import com.heu.mallchat.common.user.dao.ItemConfigDao;
import com.heu.mallchat.common.user.dao.UserBackpackDao;
import com.heu.mallchat.common.user.dao.UserDao;
import com.heu.mallchat.common.user.domain.entity.*;
import com.heu.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.heu.mallchat.common.user.domain.enums.ItemEnum;
import com.heu.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.heu.mallchat.common.user.domain.vo.req.BlackReq;
import com.heu.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.heu.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.heu.mallchat.common.user.service.UserService;
import com.heu.mallchat.common.user.service.adapter.UserAdapter;
import com.heu.mallchat.common.user.service.cache.ItemCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    private ItemCache itemCache;

    @Autowired
    private ItemConfigDao itemConfigDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private BlackDao blackDao;

    @Override
    public Long register(User insertUser) {
        userDao.save(insertUser);
        //用户注册的事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, insertUser));
        return insertUser.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer modifyNameCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user, modifyNameCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        AssertUtil.isEmpty(oldUser, "用户名已存在,请换个名字");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "改名卡不够了哦~~~");
        // 使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if (success) {
            //改名
            userDao.modifyName(uid, name);
        }
    }

    @Override
    public List<BadgeResp> badges(Long uid) {
        //查询所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        //查询用户拥有的徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemId(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        //查询用户佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs, backpacks, user);
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        //确保有徽章
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(firstValidItem, "没有该徽章");
        //确保这个物品是徽章
        ItemConfig itemConfig = itemConfigDao.getById(firstValidItem.getItemId());
        AssertUtil.equal(itemConfig.getType(), ItemTypeEnum.BADGE.getType(), "只有徽章才能佩戴");
        userDao.wearingBadge(uid, itemId);

    }

    @Override
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black black = new Black();
        black.setType(BlackTypeEnum.UID.getType());
        black.setTarget(uid.toString());
        blackDao.save(black);
        User byId = userDao.getById(uid);
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getCreateIp).orElse(null));
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getUpdateIp).orElse(null));

        applicationEventPublisher.publishEvent(new UserBlackEvent(this, byId));
    }

    private void blackIp(String ip) {
        if(StringUtils.isBlank(ip)) {
            return;
        }
        try{
            Black insert = new Black();
            insert.setType(BlackTypeEnum.IP.getType());
            insert.setTarget(ip);
            blackDao.save(insert);
        }catch (Exception e) {

        }

    }
}
