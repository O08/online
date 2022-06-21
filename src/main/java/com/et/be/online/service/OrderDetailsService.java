package com.et.be.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.et.be.online.domain.mo.OrderDetails;

public interface OrderDetailsService extends IService<OrderDetails> {
    OrderDetails getOrderDetailsByTradeNo(String tradeNo);
}
