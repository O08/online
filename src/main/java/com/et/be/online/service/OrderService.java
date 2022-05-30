package com.et.be.online.service;

import com.egzosn.pay.paypal.v2.bean.PayPalOrder;
import com.et.be.online.domain.vo.OrderSummaryVO;

public interface OrderService {
    OrderSummaryVO orderSummary();

    PayPalOrder newPayPalOrder(Long orderDetailId);

    /**
     * new order
     * @param addressId
     * @return order details id
     */
    Long newOrder(Long addressId);
}
