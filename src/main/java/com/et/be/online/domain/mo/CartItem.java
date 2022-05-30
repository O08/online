package com.et.be.online.domain.mo;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (cart_item)实体类
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("cart_item")
public class CartItem extends Model<CartItem> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
	private Long id;
    /**
     * 会话
     */
    private Long sessionId;
    /**
     * 产品
     */
    private String productCode;
    /**
     * 数量
     */
    private Integer quantity;

    /**
     *  商品单项合计价格
     */
    private Double total;
    /**
     * 创建日期
     */
    private Date createdAt;
    /**
     * 修改日期
     */
    private Date modifiedAt;

}