package com.et.be.online.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "商品管理", description = "商品场景")
public class ProductManagementListVO {
    @ApiModelProperty("商品分类")
    private String category;

    @ApiModelProperty("商品编码")
    private String productCode;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品描述")
    private String productDesc;

    @ApiModelProperty("商品原价")
    private Double  price;

    @ApiModelProperty("商品库存")
    private int  inventory;

    @ApiModelProperty("折扣")
    private Double  discountPercent;

    @ApiModelProperty("商品封面图片")
    private String featureImage;

    @ApiModelProperty("SKU")
    private String SKU;

    @ApiModelProperty("创建日期")
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonFormat( pattern = "yyyy-MM-dd")
    @ApiModelProperty("修改日期")
    private Date modifiedAt;

    // 回显 下拉
    @ApiModelProperty("分类id")
    private String categoryId;
    // 回显 下拉
    @ApiModelProperty("优惠id")
    private String discountId;

    @ApiModelProperty("产品url")
    private String productUrl;


}
