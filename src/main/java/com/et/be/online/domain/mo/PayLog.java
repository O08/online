package com.et.be.online.domain.mo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (pay_log)实体类
 *
 * @author kk
 * @since 2022-05-12 15:02:42
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("pay_log")
public class PayLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 支付上下文
     */
    private String payContext;
    /**
     * 支付平台返回消息
     */
    private String payMessage;
    /**
     * 创建时间
     */
    private Date createdate;
    /**
     * 修改时间
     */
    private Date modifydate;

}
