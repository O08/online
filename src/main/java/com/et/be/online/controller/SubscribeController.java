package com.et.be.online.controller;

import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.online.domain.dto.ContactUsDTO;
import com.et.be.online.service.SubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

@Api(value = "电商-客户", tags = "电商")
@RequestMapping("api/v1/online")
@RestController
@Slf4j
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;


    @ApiOperation("订阅newsletter")
    @ResponseBody
    @PostMapping(value = "subscribeNewsLetter")
    public ResponseVO featuredProducts(@RequestBody String email) throws UnsupportedEncodingException {

        subscribeService.subscribeNewsLetter( URLDecoder.decode(email,"UTF-8"));
        return new ResponseVO("success");
    }


    @ApiOperation("客户反馈")
    @ResponseBody
    @PostMapping(value = "contactus")
    public ResponseVO contactus(@RequestBody ContactUsDTO contactUsDTO) {

        subscribeService.handleContactUsMessage(contactUsDTO);
        return new ResponseVO("success");
    }
}
