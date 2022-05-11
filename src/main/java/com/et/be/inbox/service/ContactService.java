package com.et.be.inbox.service;

import com.et.be.inbox.domain.mo.Contact;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactService {
    List<Contact> queryByOwnerEmail(String email);
    void addContact(String ownerEmail,String contactEmail);

}
