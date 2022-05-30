package com.et.be.online.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@ApiModel(value = "图片返回", description = "商品场景")
@Accessors(chain = true)
@NoArgsConstructor
public class ImageVO {


    @ApiModelProperty("文件名")
    private String fileUrl;

    @ApiModelProperty("缩放文件名")
    private String scaleFileUrl;
}
