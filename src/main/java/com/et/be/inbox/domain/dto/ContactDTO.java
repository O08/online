package com.et.be.inbox.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel(value = "联系人", description = "联系人dto")
@Accessors(chain = true)
public class ContactDTO {
    @ApiModelProperty("备忘人")
    private String ownerEmail;

    @ApiModelProperty("联系人")
    private String contactEmail;
}
