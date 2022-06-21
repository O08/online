package com.et.be.online.enums;

/**
 * 配送状态枚举
 */
public enum DeliveryStatusEnum {
   ON_HOLD, // 订单延期
   DELIVERED, // 配送成功
   CANCELLED, // 订单已取消
   PRE_TRANSIT, // Label is created and the shipping provider is about to pick up the package.
   IN_TRANSIT, // The shipment was accepted by the shipping provider and its on the way
   AVAILABLE_FOR_PICKUP, //The shipment is ready to by picked up.
   OUT_FOR_DELIVERY, // Carrier is about to deliver the shipment
   Exception, // Other exceptions.
}
