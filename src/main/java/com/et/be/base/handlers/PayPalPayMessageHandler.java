package com.et.be.base.handlers;

import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.paypal.v2.api.PayPalPayService;
import com.et.be.online.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * PayPal支付回调处理器
 * Created by ZaoSheng on 2016/6/1.
 *
 */
@Component
public class PayPalPayMessageHandler implements PayMessageHandler<PayMessage, PayPalPayService> {


    @Autowired
    private PayLogService payLogService;


    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayPalPayService payService) throws PayErrorException {
        Map<String, Object> message = payMessage.getPayMessage();
        // 存储支付返回
        this.payLogService.createPayLog(context.toString(),message.toString());

        String tradeNo = (String) message.get("tradeNo");

         payService.ordersCapture(tradeNo);

        return payService.getPayOutMessage("fail", "失败");
    }
}
