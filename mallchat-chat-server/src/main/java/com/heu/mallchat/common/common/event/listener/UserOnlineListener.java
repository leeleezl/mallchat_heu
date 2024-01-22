package com.heu.mallchat.common.common.event.listener;

import com.heu.mallchat.common.common.enums.ChatActiveStatusEnum;
import com.heu.mallchat.common.common.event.UserOnlineEvent;
import com.heu.mallchat.common.user.dao.UserDao;
import com.heu.mallchat.common.user.domain.entity.User;
import com.heu.mallchat.common.user.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserOnlineListener {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IpService ipService;

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDb(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(ChatActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        //更新用户ip详情
        ipService.refreshIpDetailAsync(user.getId());

    }


}
