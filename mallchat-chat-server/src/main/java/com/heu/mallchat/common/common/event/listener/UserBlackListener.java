package com.heu.mallchat.common.common.event.listener;

import com.heu.mallchat.common.common.enums.ChatActiveStatusEnum;
import com.heu.mallchat.common.common.event.UserBlackEvent;
import com.heu.mallchat.common.common.event.UserOnlineEvent;
import com.heu.mallchat.common.user.dao.UserDao;
import com.heu.mallchat.common.user.domain.entity.User;
import com.heu.mallchat.common.user.service.IpService;
import com.heu.mallchat.common.user.service.cache.UserCache;
import com.heu.mallchat.common.websocket.service.WebSocketService;
import com.heu.mallchat.common.websocket.service.adapter.WebsocketAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class UserBlackListener {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IpService ipService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private UserCache userCache;

    @Async
    @EventListener(classes = UserBlackEvent.class)
    public void sendMsg(UserBlackEvent event) {
        User user = event.getUser();
        webSocketService.sendMsgToAll(WebsocketAdapter.buildBlack(user));
    }

    @Async
    @EventListener(classes = UserBlackEvent.class)
    public void changeUserStatus (UserBlackEvent event) {
        userDao.invalidUid(event.getUser().getId());
    }
    @Async
    @EventListener(classes = UserBlackEvent.class)
    public void EvictCache (UserBlackEvent event) {
        userCache.evictBlackMap();
    }

}
