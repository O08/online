package com.et.be.online.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "客户信息入参", description = "登录安全")
public class CustomerDTO {
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("电话")
    private String phone;
    private String time;
}
