package com.et.be.online.enums;

// 订单状态枚举
public enum OrderStatusEnum {
    CREATING, // 订单创建中
    CREATED, // 已创建订单
    PAID, // 已支付
    SHIPPED, // 已发货
    COMPLETED,// 订单履约完成
    CANCELLED, //取消订单
    FAIL,// 失败
    INVALID // 失效

}
