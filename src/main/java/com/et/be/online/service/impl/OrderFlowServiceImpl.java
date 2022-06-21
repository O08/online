package com.et.be.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.et.be.base.security.UserInfo;
import com.et.be.online.domain.mo.Customer;
import com.et.be.online.domain.mo.OrderDetails;
import com.et.be.online.domain.mo.OrderFlow;
import com.et.be.online.domain.mo.PaymentDetails;
import com.et.be.online.mapper.OrderDetailsMapper;
import com.et.be.online.mapper.OrderFlowMapper;
import com.et.be.online.mapper.PaymentDetailsMapper;
import com.et.be.online.service.CustomerService;
import com.et.be.online.service.OrderFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderFlowServiceImpl extends ServiceImpl<OrderFlowMapper, OrderFlow> implements OrderFlowService {
    @Autowired
    private CustomerService customerService;



    @Autowired
    private PaymentDetailsMapper paymentDetailsMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;
    @Override
    public int insertOrderFlow(Long orderDetailId, String stage) {
        // get order details id
        OrderDetails orderDetails = orderDetailsMapper.selectById(orderDetailId);

        // insert
        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setOrderId(orderDetailId)
                .setStage(stage)
                .setUserId(orderDetails.getUserId());
        int cnt = this.baseMapper.insert(orderFlow);
        return cnt;
    }

    @Override
    public int insertOrderFlowCommon(Long id, String stage) {
        // insert
        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setOrderId(id)
                .setStage(stage)
                .setUserId(id);
        int cnt = this.baseMapper.insert(orderFlow);
        return cnt;
    }


    @Override
    public int deleteOrderFlow(Long id, String stage) {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        LambdaQueryWrapper<OrderFlow> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(OrderFlow::getOrderId,id)
                .eq(OrderFlow::getStage,stage)
                .eq(OrderFlow::getUserId,customer.getId());
        return this.baseMapper.delete(wrapper);
    }
}
