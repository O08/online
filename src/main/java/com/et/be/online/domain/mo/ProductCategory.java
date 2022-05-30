package com.et.be.online.domain.mo;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (product_category)实体类
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("product_category")
public class ProductCategory extends Model<ProductCategory> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
	private Long id;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 分类说明
     */
    private String categoryDesc;
    /**
     * 创建日期
     */
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date createdAt;
    /**
     * 修改日期
     */
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date modifiedAt;
    /**
     * 删除日期
     */
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date deletedAt;

}