package com.et.be.online.domain.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "商品", description = "商品场景")
public class ProductRO {
    @ApiModelProperty("商品编码")
    private String productCode;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("折扣")
    private Double  discountPercent;

    @ApiModelProperty("商品原价")
    private Double  price;

    @ApiModelProperty("商品封面图片")
    private String featureImage;

    @ApiModelProperty("产品url")
    private String productUrl;

    @ApiModelProperty("商品介绍")
    private String productDesc;

}
