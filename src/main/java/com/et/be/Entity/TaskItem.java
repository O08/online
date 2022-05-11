package com.et.be.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("task_item")
@ApiModel(value = "物品对象", description = "物品信息")
@Accessors(chain = true)
public class TaskItem {
    @TableId(type = IdType.AUTO)
    private Long id ;

    @ApiModelProperty("物品名称")
    private String itemname;

    @ApiModelProperty("物品信息")
    private String iteminfo;

    @ApiModelProperty("物品预算")
    private int itemBudget;

    @ApiModelProperty("归属人邮箱")
    private String ownerEmail;

    @ApiModelProperty("委托人人邮箱")
    private String executorEmail;

    @ApiModelProperty("是否需要代理人")
    private boolean needAgent;


    @ApiModelProperty("物品状态")
    private int itemFlag;

    @ApiModelProperty("上架时间")
    private Date createdate;

    private Date modifydate;

}
