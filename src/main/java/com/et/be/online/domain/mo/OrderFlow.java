package com.et.be.online.domain.mo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 订单流转控制
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_flow")
public class OrderFlow {
    /**
     * 用户
     */
    private Long userId;

    /**
     * 订单明细
     */
    private Long orderId;

    /**
     * order flow stage
     */
    private String stage;


}
