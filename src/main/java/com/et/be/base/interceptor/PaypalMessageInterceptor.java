package com.et.be.base.interceptor;

import com.egzosn.pay.common.api.PayMessageInterceptor;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.paypal.v2.api.PayPalPayService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaypalMessageInterceptor implements PayMessageInterceptor<PayMessage,PayPalPayService> {
    @Override
    public boolean intercept(PayMessage payMessage, Map<String, Object> map, PayPalPayService payPalPayService) throws PayErrorException {
        return true;
    }
}
