package com.et.be.online.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "联系", description = "客户反馈")
public class ContactUsDTO {
    @ApiModelProperty("姓")
    private String firstName;

    @ApiModelProperty("名")
    private String lastName;
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    private String phoneNumber;
    @ApiModelProperty("信息")
    private String message;

}
