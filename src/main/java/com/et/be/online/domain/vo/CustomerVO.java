package com.et.be.online.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@ApiModel(value = "用户信息", description = "商品场景")
@Accessors(chain = true)
@NoArgsConstructor
public class CustomerVO {


    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("姓")
    private String firstName;

    @ApiModelProperty("产品码")
    private String lastName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("创建日期")
    private Date createdAt;

    @ApiModelProperty("修改日期")
    private Date modifiedAt;
}
