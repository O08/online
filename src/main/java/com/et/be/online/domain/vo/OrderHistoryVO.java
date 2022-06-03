package com.et.be.online.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@ApiModel(value = "历史订单返回", description = "商品场景")
@Accessors(chain = true)
@NoArgsConstructor
public class OrderHistoryVO {
    @ApiModelProperty("下单时间")
    private Date createdAt;

    @ApiModelProperty("商品小计")
    private Double subTotal;

    @ApiModelProperty("税")
    private Double tax;

    @ApiModelProperty("配送费")
    private Double shipping;

    @ApiModelProperty("产品名称")
    private String productName;


    @ApiModelProperty("商品封面图片")
    private String featureImage;

    @ApiModelProperty("商品Url")
    private String productUrl;

    @ApiModelProperty("订单编号")
    private String no;
}
