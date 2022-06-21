package com.et.be.online.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "密码重置", description = "认证")
public class PasswordResetDTO {
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "token")
    private String token;
}
