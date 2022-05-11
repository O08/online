package com.et.be.inbox.controller;

import com.et.be.inbox.domain.dto.ContactDTO;
import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.inbox.service.ContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "平台交流功能-联系人", tags = "平台交流功能")
@RequestMapping("api/v1/inbox")
@RestController
@Slf4j
public class ContactController {

    @Autowired
    private ContactService contactService;

    @ApiOperation("联系人查询")
    @ApiImplicitParam(paramType = "header", name = "identity", value = "登录用户id", dataType = "String", required = true)
    @ResponseBody
    @PostMapping(value = "displayContact")
    public ResponseVO displayContact(String email) {
        return new ResponseVO(contactService.queryByOwnerEmail(email));
    }

    @ApiOperation("联系人添加")
    @ResponseBody
    @PostMapping(value = "addContact")
    public ResponseVO addContact(@RequestBody ContactDTO contactDTO) {
        contactService.addContact(contactDTO.getOwnerEmail() ,contactDTO.getContactEmail());
        return new ResponseVO("success");
    }

}
