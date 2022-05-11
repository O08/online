package com.et.be.Feature.common.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("sys_ticket")
@ApiModel(value = "系统入场券", description = "系统准入控制")
@Accessors(chain = true)
public class Ticket {
    @TableId(type = IdType.AUTO)
    private Long id ;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("准入码")
    private String accessCode;

    @ApiModelProperty("准入状态")
    private int flag;

    @ApiModelProperty("发放时间")
    private Date createdate;

    @ApiModelProperty("修改时间时间")
    private Date modifydate;
}
