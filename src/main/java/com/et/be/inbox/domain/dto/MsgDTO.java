package com.et.be.inbox.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel(value = "消息", description = "消息dto")
@Accessors(chain = true)
public class MsgDTO {
    @ApiModelProperty("消息发送人")
    private String sender;

    @ApiModelProperty("消息接受人")
    private String receiver;

    @ApiModelProperty("消息内容")
    private String content;

}
