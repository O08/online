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
 * (customer_address)实体类
 *
 * @author kk
 * @since 2022-05-09 22:40:01
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("customer_address")
public class CustomerAddress extends Model<CustomerAddress> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
	private Long id;
    /**
     * 用户
     */
    private Long userId;
    /**
     * 收件人
     */
    private String receiver;

    /**
     * 地址
     */
    private String addressLine1;
    /**
     * 地址
     */
    private String addressLine2;
    /**
     * 城市
     */
    private String city;
    /**
     * 邮编
     */
    private String postalCode;
    /**
     * 州
     */
    private String state;
    /**
     * 国家
     */
    private String country;
    /**
     * 电话
     */
    private String telephone;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 创建日期
     */
    private Date createdAt;
    /**
     * 修改日期
     */
    private Date modifiedAt;

}