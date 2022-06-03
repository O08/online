package com.et.be.online.service;

import com.egzosn.pay.paypal.v2.bean.PayPalOrder;
import com.et.be.online.domain.vo.OrderHistoryVO;
import com.et.be.online.domain.vo.OrderSummaryVO;

import java.util.List;

public interface OrderService {
    OrderSummaryVO orderSummary();

    PayPalOrder newPayPalOrder(Long orderDetailId);

    /**
     * new order
     * @param addressId
     * @return order details id
     */
    Long newOrder(Long addressId);

    List<OrderHistoryVO> orderHistory();

    void newPaymentDetails(Long orderDetailsId, String tradeNo);
}
