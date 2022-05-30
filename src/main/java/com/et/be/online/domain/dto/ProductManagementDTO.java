package com.et.be.online.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ApiModel(value = "商品管理", description = "商品")
public class ProductManagementDTO {
    @ApiModelProperty("商品分类")
    @NotEmpty
    private String productCategory;

    @ApiModelProperty("商品码")
    @NotEmpty
    private String productCode;

    @ApiModelProperty("商品名")
    @NotEmpty
    private String productName;

    @ApiModelProperty("商品明细")
    private String productDesc;

    @ApiModelProperty("产品定价")
    @NotEmpty
    private String productPrice;

    @ApiModelProperty("库存")
    @NotEmpty
    private String productInventory;

    @ApiModelProperty("折扣项")
    private String productDiscount;

    @ApiModelProperty("SKU")
    @NotEmpty
    private String SKU;

    @ApiModelProperty("SKU")
    @NotEmpty
    private String featureImage;

    @ApiModelProperty("商品url")
    private String productUrl;


}
