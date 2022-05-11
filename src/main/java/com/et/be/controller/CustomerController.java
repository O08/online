package com.et.be.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.et.be.Feature.common.Entity.RichTextConfig;
import com.et.be.Feature.common.dto.TicketDTO;
import com.et.be.Feature.common.service.RichTextConfigService;
import com.et.be.Feature.common.service.TicketService;
import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.inbox.utils.SecureCheckUtil;
import com.et.be.inbox.utils.UUIDUtils;
import com.et.be.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "平台基础功能-授信", tags = "基础功能")
@RequestMapping("api/v1/common")
@RestController
@Slf4j
public class CustomerController {
    @Autowired
    TicketService ticketService;
    @Autowired
    RichTextConfigService textConfigService;

    @Autowired
    EmailUtil emailUtil;

    @ApiOperation("准入校验")
    @ResponseBody
    @PostMapping(value = "checkTicket")
    public ResponseVO checkTicket(@RequestBody TicketDTO ticketDTO) {
        String identity = "" ;
        if(ObjectUtil.isNotEmpty(ticketService.getTicket(ticketDTO.getEmail(),ticketDTO.getAccessCode()))){
            identity =  SecureCheckUtil.encryptIdentity(ticketDTO.getEmail());
        }
        return new ResponseVO(identity);
    }

    @ApiOperation("发放准入券")
    @ResponseBody
    @PostMapping(value = "sendTicket")
    public ResponseVO sendTicket(String email){
        // 生成准入码并存放
        String accessCode = UUIDUtils.create();
        ticketService.saveTicket(email, accessCode);
        // 向用户发送准入码
        sendAccessCode(email,accessCode);
        return new ResponseVO("success");
    }

    private void sendAccessCode(String email,String accessCode){
        // 查询模版
        RichTextConfig emailHtmlConfig = textConfigService.getRichTextConfig("emailHtml", "1");
        if(StrUtil.isEmptyIfStr(emailHtmlConfig)){
            return ;
        }
        // 生成邮件内容
       String content = emailHtmlConfig.getContent()
                .replace("#{email}",email)
                .replace("#{accessCode} ",accessCode);
        // 发送邮件 1 html 2 发送对象 3 主题
        emailUtil.sendHtmlEmail(content,email,"AlphaSpace 系统授权");
    }


}
