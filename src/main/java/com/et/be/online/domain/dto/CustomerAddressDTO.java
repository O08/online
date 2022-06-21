package com.et.be.online.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ApiModel(value = "配送地址入参", description = "商品")
public class CustomerAddressDTO {
    @ApiModelProperty("id ,修改地址时要传参")
    private Long id;

    @ApiModelProperty("地址")
    private String addressLine1;

    @ApiModelProperty("地址")
    private String addressLine2;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("邮编")
    @NotEmpty
    private String postalCode;

    @ApiModelProperty("州")
    private String state;

    @ApiModelProperty("国家")
    @NotEmpty
    private String country;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("手机")
    @NotEmpty
    private String mobile;

    @ApiModelProperty("收件人")
    @NotEmpty
    private String receiver;

}
