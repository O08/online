package com.et.be.inbox.domain.mo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("contact")
@ApiModel(value = "联系人", description = "客户通讯录")
@Accessors(chain = true)
public class Contact {
    @TableId(type = IdType.AUTO)
    private Long id ;

    @ApiModelProperty("归属人邮箱")
    private String ownerEmail;

    @ApiModelProperty("联系人邮箱")
    private String contactEmail;

    @ApiModelProperty("创建时间")
    private Date createdate;

    private Date modifydate;

}
