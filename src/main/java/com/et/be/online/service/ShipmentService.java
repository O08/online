package com.et.be.online.service;

import com.et.be.online.domain.mo.Shipment;

public interface ShipmentService {
    /**
     * 获取运单信息
     * @param shipNo 运单号
     * @return
     */
    Shipment getShipment(String shipNo) throws Exception;

    /**
     * 创建订单的运单信息
     * @param shipment
     */
    void createShipment(Shipment shipment);

}
