package com.et.be.online.service;

import com.et.be.online.domain.mo.PaypalV2Order;
import com.et.be.online.domain.vo.OrderHistoryVO;
import com.et.be.online.domain.vo.OrderSummaryVO;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    OrderSummaryVO orderSummary();


    /**
     * new order
     * @param addressId
     * @return order details id
     */
    Long newOrder(Long addressId);

    List<OrderHistoryVO> orderHistory();

    void newPaymentDetails(Long orderDetailsId, String tradeNo);

    void paypalOrdersCapture(String tradeNo) throws IOException;

    PaypalV2Order newPaypalV2Order(Long orderDetailId);

    boolean payVerify(Long id,String stage);

    void updatePaymentDetails(Long orderDetailsId, String tradeNo);

    void cancelOrderItems(String tradeNo);
}
