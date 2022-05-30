package com.et.be.online.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ApiModel(value = "商品目录", description = "商品分类")
public class CategoryDTO {
    @ApiModelProperty("名称")
    @NotEmpty
    private String categoryName;
    @ApiModelProperty("描述")
    private String categoryDesc;
}
