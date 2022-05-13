package com.et.be.online.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.online.domain.mo.Shipment;
import com.et.be.online.mapper.ShipmentMapper;
import com.et.be.online.service.ShipmentService;
import com.et.be.util.DeleveryUtil;
import com.kuaidi100.sdk.pojo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    @Autowired
    private ShipmentMapper shipmentMapper;

    @Autowired
    private DeleveryUtil deleveryUtil;

    /**
     * 订单运单查询
     * @param shipNo 运单号
     * @return
     */
    @Override
    public Shipment getShipment(String shipNo) throws Exception {
        QueryWrapper<Shipment> queryWrapper = Wrappers.query();
        queryWrapper.eq("ship_no",shipNo);
        Shipment shipment = shipmentMapper.selectOne(queryWrapper);
        // 控制每一单查询频率至少在半小时以上，否则会造成锁单。 当前设置为1h
        boolean interval1h = DateUtil.isIn(new Date(),shipment.getModifydate(),
                  DateUtil.offsetHour(shipment.getModifydate(),1));
        // 间隔一小时之内 返回平台数据状态
        if(interval1h){
            return shipment;
        }
        // 间隔大于一小时 请求最新数据 更新平台数据
        HttpResult httpResult = deleveryUtil.queryDelevery(shipment.getSupplier(), shipment.getShipNo(), shipment.getReceiverPhone());
        shipment.setTrackers(httpResult.toString());
        shipmentMapper.updateById(shipment);

        return shipment;
    }

    /**
     * 创建运单的订单信息
     * @param shipment
     */
    @Override
    public void createShipment(Shipment shipment) {
        shipmentMapper.insert(shipment);
    }

}
