package com.et.be.online.domain.mo;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (product)实体类
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("product")
public class Product extends Model<Product> implements Serializable {
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
     * 产品url
     */
    private String productUrl;

    /**
     * 单项商品重量
     */
    private int weight;


}