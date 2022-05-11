package com.et.be.inbox.controller;

import com.et.be.inbox.domain.dto.MsgDTO;
import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.inbox.service.ChatRecordService;
import com.et.be.inbox.service.ContactService;
import com.et.be.inbox.service.MsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "平台交流功能-消息", tags = "平台交流功能")
@RequestMapping("api/v1/inbox")
@RestController
@Slf4j
public class MsgController {

    @Autowired
    private MsgService msgService;

    @Autowired
    private ContactService contactService;

    @ApiOperation("发送消息")
    @ResponseBody
    @PostMapping(value = "sendMsg")
    public ResponseVO sendMsg(@RequestBody MsgDTO msgDTO) {
        // 持久化消息
        msgService.addMsg(msgDTO);
        return new ResponseVO("success");
    }

    @ApiOperation("消息列表")
    @ResponseBody
    @ApiImplicitParam(paramType = "header", name = "identity", value = "登录用户id", dataType = "String", required = true)
    @PostMapping(value = "listMsg")
    public ResponseVO listMsg(@RequestBody MsgDTO msgDTO) {

        return new ResponseVO(msgService.listMsg(msgDTO));
    }
}
