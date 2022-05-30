package com.et.be.online.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "分页", description = "分页查询")
public class PageDTO {
    @ApiModelProperty(value = "当前页")
    private Long current;
    @ApiModelProperty(value = "分页大小")
    private Long size;

    @ApiModelProperty(value = "分页查询参数")
    private String queryParams;
}
