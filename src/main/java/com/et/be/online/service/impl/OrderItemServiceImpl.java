package com.et.be.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.et.be.online.domain.mo.OrderItem;
import com.et.be.online.mapper.OrderItemMapper;
import com.et.be.online.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
}
