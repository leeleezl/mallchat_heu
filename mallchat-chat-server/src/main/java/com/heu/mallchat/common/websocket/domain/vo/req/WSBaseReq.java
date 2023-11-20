package com.heu.mallchat.common.websocket.domain.vo.req;

import lombok.Data;

@Data
public class WSBaseReq {

    /**
     * @See com.heu.mallchat.common.websocket.domain.enums.WSBaseReqTypeEnum
     */
    private Integer type;
    private String data;

}
