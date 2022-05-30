package com.et.be.online.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ApiModel(value = "购物车入参", description = "商品")
public class CartItemDTO {
    @ApiModelProperty("产品编码")
    @NotEmpty
    private String productCode;

    @ApiModelProperty("数量")
    @Min(1)
    @Max(3)
    private Integer quantity;
}
