package com.heu.mallchat.common.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    NO(0, "否"),
    YES(1,"是"),
    ;

    private final Integer status;
    private final String desc;

}
