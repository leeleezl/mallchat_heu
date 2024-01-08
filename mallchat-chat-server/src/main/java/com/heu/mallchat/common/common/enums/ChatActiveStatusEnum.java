package com.heu.mallchat.common.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatActiveStatusEnum {

    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    ;

    private Integer status;
    private String desc;

}
