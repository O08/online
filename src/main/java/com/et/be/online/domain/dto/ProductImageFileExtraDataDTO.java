package com.et.be.online.domain.dto;

import com.et.be.online.domain.vo.ImageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "产品图片", description = "产品")
public class ProductImageFileExtraDataDTO {
    @ApiModelProperty(value = "产品码")
    private String productCode;
    @ApiModelProperty(value = "最新图片集")
    private List<ImageVO> images;
}
