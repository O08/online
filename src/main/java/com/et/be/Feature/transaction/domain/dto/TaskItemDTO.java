package com.et.be.Feature.transaction.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskItemDTO {

    @ApiModelProperty("物品名称")
    private String itemname;

    @ApiModelProperty("物品信息")
    private String iteminfo;

    @ApiModelProperty("物品预算")
    private int itemBudget;

    @ApiModelProperty("归属人邮箱")
    private String ownerEmail;

    @ApiModelProperty("是否需要代理人")
    private boolean needAgent;



}
