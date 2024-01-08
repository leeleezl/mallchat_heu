package com.heu.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class WearingBadgeReq {

    @ApiModelProperty("徽章Id")
    @NotNull
    private Long itemId;

}
