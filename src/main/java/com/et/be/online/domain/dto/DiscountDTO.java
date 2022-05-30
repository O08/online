package com.et.be.online.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@ApiModel(value = "商品优惠", description = "商品")
public class DiscountDTO {
    @ApiModelProperty("名称")
    @NotEmpty
    private String discountName;

    @ApiModelProperty("优惠说明")
    private String discountDesc;

    @ApiModelProperty("折扣")
    @NotNull
    private Double discountPercent;

    @ApiModelProperty("优惠活动 true 启用优惠 false 未启用")
    @NotNull
    private Boolean active;
}
