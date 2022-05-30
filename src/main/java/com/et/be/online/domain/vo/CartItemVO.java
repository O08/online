package com.et.be.online.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 购物车
 */
@Data
@ApiModel(value = "购物车", description = "商品场景")
@Accessors(chain = true)
@NoArgsConstructor
public class CartItemVO {
    @ApiModelProperty("产品码")
    private String productCode;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("折扣")
    private Double  discountPercent;

    @ApiModelProperty("商品原价")
    private Double  price;

    @ApiModelProperty("商品封面图片")
    private String featureImage;

    @ApiModelProperty("商品Url")
    private String productUrl;

    @ApiModelProperty("商品数量")
    private Integer  quantity;



}
