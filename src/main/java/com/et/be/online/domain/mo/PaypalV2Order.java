package com.et.be.online.domain.mo;

import com.paypal.orders.PurchaseUnitRequest;
import lombok.Data;

import java.util.List;
@Data
public class PaypalV2Order {
    // 商品列表
    private List<PurchaseUnitRequest> purchaseUnitRequests;
    //单后返回对应单号
    private String tradeNo;
}
