package com.et.be.Feature.common.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "系统入场券DTO", description = "系统准入控制")
public class TicketDTO {
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("准入码")
    private String accessCode;
}
