package com.et.be.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.et.be.online.domain.mo.PaymentDetails;

public interface PaymentDetailsService extends IService<PaymentDetails> {
    PaymentDetails getPaymentDetailsByTradeNo(String tradeNo);
}
