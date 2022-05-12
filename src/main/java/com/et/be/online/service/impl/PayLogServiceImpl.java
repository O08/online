package com.et.be.online.service.impl;

import com.et.be.online.domain.mo.PayLog;
import com.et.be.online.mapper.PayLogMapper;
import com.et.be.online.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PayLogServiceImpl implements PayLogService {
    @Autowired
    private PayLogMapper payLogMapper;
    @Override
    public void createPayLog(String context, String payMessage) {
        PayLog payLog = new PayLog();
        payLog.setPayMessage(payMessage)
                .setPayContext(context)
                .setCreatedate(new Date())
                .setModifydate(new Date());
        payLogMapper.insert(payLog);
    }
}
