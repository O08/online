package com.et.be.online.domain.mo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("sys_richtext_config")
@ApiModel(value = "系统富文本", description = "配置富文本如邮件html ")
@Accessors(chain = true)
public class RichTextConfig {
    @TableId(type = IdType.AUTO)
    private Long id ;
    @ApiModelProperty("富文本类型")
    private String contentType;

    @ApiModelProperty("富文本编号")
    private String contentNo;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("创建时间")
    private Date createdate;

    @ApiModelProperty("修改时间")
    private Date modifydate;
}
