package com.heu.mallchat.common.common.event;

import com.heu.mallchat.common.user.domain.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserOnlineEvent extends ApplicationEvent {
    private User user;
    public UserOnlineEvent(Object source,User user) {
        super(source);
        this.user = user;
    }
}
