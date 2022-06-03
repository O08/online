package com.et.be.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.et.be.online.domain.mo.OrderItem;
import com.et.be.online.domain.vo.OrderHistoryVO;

import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    List<OrderHistoryVO> orderHistory(Long userId);
}
