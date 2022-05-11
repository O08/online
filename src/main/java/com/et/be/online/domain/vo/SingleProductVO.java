package com.et.be.online.domain.vo;

import com.et.be.online.domain.mo.Specification;
import com.et.be.online.domain.ro.ProductRO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "商品明细", description = "商品场景")
public class SingleProductVO {
    @ApiModelProperty("商品基础信息")
    private ProductRO product;

    @ApiModelProperty("商品规格")
    private Specification specification;
}
