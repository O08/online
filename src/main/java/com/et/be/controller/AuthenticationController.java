package com.et.be.controller;


import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.inbox.utils.SecureCheckUtil;
import com.et.be.online.domain.mo.PublicKeyInfo;
import com.et.be.online.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "平台基础功能-授信2", tags = "基础功能")
@RequestMapping("api/v1/auth")
@RestController
@Slf4j
public class AuthenticationController {

    @Autowired
    private CustomerService customerService;

    @ApiOperation("发送公钥")
    @ResponseBody
    @PostMapping(value = "getPublicKey")
    public ResponseVO getPublicKey( ) {
        PublicKeyInfo pki = new PublicKeyInfo();
        pki.setPublicKey(SecureCheckUtil.getPublicKey() );
        pki.setTime(System.currentTimeMillis());
        return new ResponseVO(pki);
    }


    @ApiOperation("注册 api")
    @ResponseBody
    @PostMapping(value = "doSignUp")
    public ResponseVO doSignUp(String encrypted ) {
        String signUpInfo = SecureCheckUtil.decryptIdentity(encrypted);
        return new ResponseVO( customerService.doSignUp(signUpInfo));
    }




}
