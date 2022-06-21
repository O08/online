package com.et.be.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.et.be.online.domain.mo.OrderFlow;

public interface OrderFlowService extends IService<OrderFlow> {
    /**
     * insert order flow
     * @param orderDetailId order_details id
     * @param stage order status
     * @return insert return
     */
    int insertOrderFlow(Long orderDetailId ,String stage);

    /**
     * insert order flow common
     * @param id your need to controller
     * @param stage
     * @return
     */
    int insertOrderFlowCommon(Long id ,String stage);




    /**
     * delete order flow
     * @param id order_details id or other id
     * @param stage delete return
     * @return
     */
    int deleteOrderFlow(Long id,String stage);
}
