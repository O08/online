package com.et.be.online.domain.mo;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (order_item)实体类
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_item")
public class OrderItem extends Model<OrderItem> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
	private Long id;
    /**
     * 订单明细
     */
    private Long orderId;
    /**
     * 产品
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 折扣
     */
    private Double  discountPercent;

    /**
     * 商品原价
     */
    private Double  price;

    /**
     * 购买数量
     */
    private Long quantity;
    /**
     * 商品小计
     */
    private Double subTotal;
    /**
     * 税
     */
    private Double tax;
    /**
     * 配送费
     */
    private Double shipping;

    /**
     * 配送运单id
     */
    private Long shipmentId;
    /**
     * captureId papal captureId
     */
    private String captureId;

    /**
     * order status
     */
    private String orderStatus;

    /**
     * 创建日期
     */
    private Date createdAt;
    /**
     * 修改日期
     */
    private Date modifiedAt;

}