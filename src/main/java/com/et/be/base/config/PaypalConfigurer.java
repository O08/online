package com.et.be.base.config;

import com.egzosn.pay.paypal.api.PayPalConfigStorage;

public class PaypalConfigurer {

    private PayPalConfigStorage storage = new PayPalConfigStorage();

    public PayPalConfigStorage getPayPalConfigStorage(){
        storage.setClientID("AZDS0IhUZvJTO99unlvSDMfbZIP-p-UecYXZdJoweha9LFuqKXKcQIGZgfVaX6oGiAOJAUuJD7JwyTl1");
        storage.setClientSecret("EK2YaOrw3oLSDWIRzvb9BWGTjiPPhY1fFUu5ylhUsGYLc_h_dlpJ0hr_LDEkbO9MyKP2P83YcywbPaem");
        storage.setTest(true);
        //发起付款后的页面转跳地址
        storage.setReturnUrl("http://127.0.0.1:8081/payPalV2/payBackBefore.json");
        // 注意：这里不是异步回调的通知 IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
        //取消按钮转跳地址,
        storage.setCancelUrl("http://127.0.0.1:8081/payPalV2/cancel");

        return  storage;

    }


}
