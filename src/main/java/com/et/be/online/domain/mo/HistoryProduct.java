package com.et.be.online.domain.mo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("history_product")
public class HistoryProduct extends Model<Product> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
    private Long id;

    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品说明
     */
    private String productDesc;
    /**
     * SKU
     */
    private String sku;
    /**
     * 产品目录
     */
    private Long categoryId;
    /**
     * 库存
     */
    private Long inventoryId;
    /**
     * 价格
     */
    private Double price;
    /**
     * 优惠
     */
    private Long discountId;
    /**
     * 创建日期
     */
    private Date createdAt;
    /**
     * 修改日期
     */
    private Date modifiedAt;
    /**
     * 删除日期
     */
    private Date deletedAt;
    /**
     * 商品封面图片
     */
    private String featureImage;
    /**
     * 图片列表
     */
    private String images;



}