package com.et.be.online.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ApiModel(value = "订单情况", description = "商品场景")
@Accessors(chain = true)
@NoArgsConstructor
public class OrderSummaryVO {

    @ApiModelProperty("购物车商品总价")
    private Double subtotal;
    @ApiModelProperty("税")
    private Double tax;
    @ApiModelProperty("物流费用")
    private Double shipping;
    @ApiModelProperty("总价：购物车商品总价 + 税 + 物流费用  ")
    private Double total;
    @ApiModelProperty("商品清单")
    List<CartItemVO> items;

}
