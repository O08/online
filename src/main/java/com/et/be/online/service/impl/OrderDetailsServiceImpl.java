package com.et.be.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.et.be.online.domain.mo.OrderDetails;
import com.et.be.online.domain.mo.PaymentDetails;
import com.et.be.online.mapper.OrderDetailsMapper;
import com.et.be.online.mapper.PaymentDetailsMapper;
import com.et.be.online.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetails> implements OrderDetailsService {
    @Autowired
    private PaymentDetailsMapper paymentDetailsMapper;
    @Override
    public OrderDetails getOrderDetailsByTradeNo(String tradeNo) {
        // get payment details id
        LambdaQueryWrapper<PaymentDetails> paymentDetailsLambdaQueryWrapper =new LambdaQueryWrapper<>();
        paymentDetailsLambdaQueryWrapper.eq(PaymentDetails::getTradNo,tradeNo);
        PaymentDetails paymentDetails = paymentDetailsMapper.selectOne(paymentDetailsLambdaQueryWrapper);

        // get order details id
        LambdaQueryWrapper<OrderDetails> OrderDetailsWrapper =new LambdaQueryWrapper<>();
        OrderDetailsWrapper.eq(OrderDetails::getPaymentId,paymentDetails.getId());
        OrderDetails orderDetails = this.baseMapper.selectOne(OrderDetailsWrapper);
        return orderDetails;
    }
}
