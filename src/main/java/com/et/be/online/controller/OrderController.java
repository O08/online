package com.et.be.online.controller;

import com.et.be.base.vo.ResponseVO;
import com.et.be.online.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "电商-订单", tags = "电商")
@RequestMapping("api/v1/order")
@RestController
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @ApiOperation("收银订单情况")
    @ResponseBody
    @PostMapping(value = "orderSummary")
    public ResponseVO orderSummary() {
        return new ResponseVO( orderService.orderSummary());
    }

    @ApiOperation("下单")
    @ResponseBody
    @PostMapping(value = "newOrder")
    public ResponseVO newOrder(Long addressId) {
        return new ResponseVO( orderService.newOrder(addressId));
    }

    @ApiOperation("历史订单")
    @ResponseBody
    @PostMapping(value = "orderHistory")
    public ResponseVO orderHistory() {
        return new ResponseVO( orderService.orderHistory());
    }
}
