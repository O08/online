package com.et.be.online.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.et.be.online.domain.dto.ContactUsDTO;
import com.et.be.online.domain.mo.Subscription;

public interface SubscribeService extends IService<Subscription> {
    void subscribeNewsLetter(String email);
    void handleContactUsMessage(ContactUsDTO contactUsDTO);
}
