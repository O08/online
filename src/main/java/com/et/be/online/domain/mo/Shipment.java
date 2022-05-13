package com.et.be.online.domain.mo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (shipment)实体类
 *
 * @author kk
 * @since 2022-05-13 11:11:28
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("shipment")
public class Shipment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 运单的配送状态 0 - 未发货 1 - 配送中 2 - 确认收货 3 - 已签收
     */
    private String status;
    /**
     * 运单的配送状态描述 未发货 配送中 确认收货 已签收
     */
    private String statusDesc;
    /**
     * 运单的配送方式 0 - 普通快递 1 - 顺丰速运 2 - EMS 3 - 平邮/挂号信 4 - 商家配送 5 - 自提
     */
    private String shipType;
    /**
     * 运单的配送方式描述 普通快递 顺丰速运 EMS 平邮/挂号信 商家配送 自提
     */
    private String shipTypeDesc;
    /**
     * 运单的配送单号
     */
    private String shipNo;
    /**
     * 运单的配送物流供应商
     */
    private String supplier;
    /**
     * 运单的配送运费
     */
    private double  amount;
    /**
     * 运单的配送商品列表
     */
    private String lineItems;
    /**
     * 运单的配送状态跟踪列表
     */
    private String trackers;
    /**
     * 收件人电话
     */
    private String  receiverPhone;
    /**
     * 寄件人电话
     */
    private String   consignorPhone;
    /**
     * 创建时间
     */
    private Date createdate;
    /**
     * 修改时间
     */
    private Date modifydate;

}