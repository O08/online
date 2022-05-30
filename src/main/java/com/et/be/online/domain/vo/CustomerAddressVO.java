package com.et.be.online.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@ApiModel(value = "用户地址信息", description = "自服务")
@Accessors(chain = true)
@NoArgsConstructor
public class CustomerAddressVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("地址")
    private String addressLine1;

    @ApiModelProperty("地址")
    private String addressLine2;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("邮编")
    private String postalCode;

    @ApiModelProperty("州")
    private String state;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("地址")
    private String fullAddress;

}
