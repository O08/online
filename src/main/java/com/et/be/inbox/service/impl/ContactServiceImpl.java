package com.et.be.inbox.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.inbox.dao.ContactDao;
import com.et.be.inbox.domain.mo.Contact;
import com.et.be.inbox.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactDao contactDao;
    @Override
    public List<Contact> queryByOwnerEmail(String email) {
        QueryWrapper<Contact> wrapper = Wrappers.query();
        wrapper.eq("owner_email",email);
        return contactDao.selectList(wrapper);

    }

    @Override
    public void addContact(String ownerEmail, String contactEmail) {
        QueryWrapper<Contact> wrapper = Wrappers.query();
        wrapper.eq("owner_email",ownerEmail)
                        .eq("contact_email",contactEmail);
        boolean exists = contactDao.selectCount(wrapper)>0;
        if(exists){
            return;
        }
        // 双方互相为好友
        Contact owner = new Contact();
        owner.setOwnerEmail(ownerEmail)
                .setContactEmail(contactEmail)
                .setCreatedate(new Date())
                .setModifydate(new Date());
        Contact contact = new Contact();
        contact.setOwnerEmail(contactEmail)
                .setContactEmail(ownerEmail)
                .setCreatedate(new Date())
                .setModifydate(new Date());
        contactDao.insert(owner);
        contactDao.insert(contact);
    }
}
