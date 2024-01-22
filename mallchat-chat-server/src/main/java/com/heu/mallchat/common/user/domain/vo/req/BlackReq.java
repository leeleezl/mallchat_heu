package com.heu.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlackReq {

    @ApiModelProperty("拉黑用户的uid")
    @NotNull
    private Long uid;

}
