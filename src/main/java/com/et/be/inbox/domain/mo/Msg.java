package com.et.be.inbox.domain.mo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("msg")
@ApiModel(value = "消息", description = "消息记录")
@Accessors(chain = true)
public class Msg {
    @TableId(type = IdType.AUTO)
    private Long id ;

    @ApiModelProperty("消息发送人")
    private String sender;

    @ApiModelProperty("消息接受人")
    private String receiver;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("创建时间")
    private Date createdate;

    private Date modifydate;
}
