package com.et.be.online.domain.mo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ApiModel(value = "注册入参", description = "用户")
public class SignUpInfo {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String fullname;
    private String time;
}
