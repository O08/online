package com.et.be.online.service.impl;

import cn.hutool.core.util.StrUtil;
import com.et.be.Feature.common.Entity.RichTextConfig;
import com.et.be.Feature.common.service.RichTextConfigService;
import com.et.be.inbox.domain.dto.ContactDTO;
import com.et.be.online.constant.RichTextConfigConstant;
import com.et.be.online.constant.SubscriptionConstant;
import com.et.be.online.domain.dto.ContactUsDTO;
import com.et.be.online.domain.mo.Subscription;
import com.et.be.online.mapper.SubscriptionMapper;
import com.et.be.online.service.SubscribeService;
import com.et.be.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SubscribeServiceImpl implements SubscribeService {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    RichTextConfigService textConfigService;

    @Autowired
    EmailUtil emailUtil;
    @Override
    public void subscribeNewsLetter(String email) {
        Subscription subscription = new Subscription();
        subscription
                .setEmail(email)
                .setSubType(SubscriptionConstant.SUB_TYPE_NEWSLETTER)
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        subscriptionMapper.insert(subscription);
    }

    /**
     * 处理用户反馈
     * @param contactUsDTO
     */
    @Override
    public void handleContactUsMessage(ContactUsDTO contactUsDTO) {
       notifySeller(contactUsDTO);
    }

    /**
     *  邮件通知用户反馈内容
     * @param contactUsDTO
     */
    private void notifySeller(ContactUsDTO contactUsDTO){
        // 查询模版
        RichTextConfig emailHtmlConfig = textConfigService.getRichTextConfig(RichTextConfigConstant.TYPE_EMAILHTML, RichTextConfigConstant.TYPE_EMAILHTML_CONTACT_US_T);
        if(StrUtil.isEmptyIfStr(emailHtmlConfig)){
            return ;
        }
        // 生成邮件内容
        String content = emailHtmlConfig.getContent()
                .replace("#{firstname}",contactUsDTO.getFirstName())
                .replace("#{lastname}",contactUsDTO.getLastName())
                .replace("#{email}",contactUsDTO.getEmail())
                .replace("#{phoneNumber}",contactUsDTO.getPhoneNumber())
                .replace("#{message} ",contactUsDTO.getMessage());
        // 发送邮件 1 html 2 发送对象 3 主题
        emailUtil.sendHtmlEmail(content,contactUsDTO.getEmail(),"AlphaSpace 系统授权");
    }
}
