package com.et.be.online.service;


import com.et.be.inbox.domain.dto.ContactDTO;
import com.et.be.online.domain.dto.ContactUsDTO;

public interface SubscribeService {
    void subscribeNewsLetter(String email);
    void handleContactUsMessage(ContactUsDTO contactUsDTO);
}
