package com.et.be.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.et.be.online.domain.mo.PaymentDetails;
import com.et.be.online.mapper.PaymentDetailsMapper;
import com.et.be.online.service.PaymentDetailsService;
import org.springframework.stereotype.Service;


@Service
public class PaymentDetailsServiceImpl extends ServiceImpl<PaymentDetailsMapper, PaymentDetails> implements PaymentDetailsService {

    @Override
    public PaymentDetails getPaymentDetailsByTradeNo(String tradeNo) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<PaymentDetails>().eq(PaymentDetails::getTradNo,tradeNo));
    }
}
