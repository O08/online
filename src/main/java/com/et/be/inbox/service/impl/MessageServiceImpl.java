package com.et.be.inbox.service.impl;

import com.et.be.inbox.annotation.ChatRecord;
import com.et.be.inbox.cache.UserCache;
import com.et.be.inbox.constant.RobotConstant;
import com.et.be.inbox.constant.StompConstant;
import com.et.be.inbox.domain.mo.User;
import com.et.be.inbox.domain.vo.MessageVO;
import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.inbox.enums.CodeEnum;
import com.et.be.inbox.enums.MessageTypeEnum;
import com.et.be.inbox.enums.inter.Code;
import com.et.be.inbox.exception.ErrorCodeException;
import com.et.be.inbox.service.MessageService;
import com.et.be.inbox.service.RobotService;
import com.et.be.inbox.utils.CheckUtils;
import com.et.be.inbox.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yanpanyi
 * @date 2019/4/18
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private SimpMessagingTemplate messagingTemplate;
    @Resource
    private RobotService robotService;

    @Override
    public void sendErrorMessage(Code code, User user) {
        log.info("发送错误信息 -> {} -> {}", code, user);
        messagingTemplate.convertAndSendToUser(user.getUserId(), StompConstant.SUB_ERROR, new ResponseVO(code));
    }

    @ChatRecord
    @Override
    public void sendMessage(String subAddress, MessageVO messageVO) throws Exception {
        if (!CheckUtils.checkSubAddress(subAddress)) {
            throw new ErrorCodeException(CodeEnum.INVALID_PARAMETERS);
        }

        messagingTemplate.convertAndSend(subAddress, buildResponseVo(messageVO));
    }

    @ChatRecord
    @Override
    public void sendMessageToUser(String[] receiver, MessageVO messageVO) throws Exception {
        if (!CheckUtils.checkReceiver(receiver)) {
            throw new ErrorCodeException(CodeEnum.INVALID_PARAMETERS);
        }

        ResponseVO responseVO = buildResponseVo(messageVO);
        for (int i = 0, len = receiver.length; i < len; i++) {
            // 将消息发送到指定用户 参数说明：1.消息接收者 2.消息订阅地址 3.消息内容
            messagingTemplate.convertAndSendToUser(receiver[i], StompConstant.SUB_USER, responseVO);
        }
    }

    private ResponseVO buildResponseVo(MessageVO messageVO) throws ErrorCodeException {
        if (messageVO == null) {
            throw new ErrorCodeException(CodeEnum.INVALID_PARAMETERS);
        }

        return new ResponseVO(messageVO);
    }

    @Async
    @Override
    public void sendMessageToRobot(String subAddress, String message, User user) throws Exception {
        log.info("user: {} -> 发送消息到机器人 -> {}", user, message);
        String robotMessage = robotService.sendMessage(user.getUserId(), message.replaceFirst(RobotConstant.prefix,
                ""));
        log.info("机器人响应结果 -> {}", robotMessage);
        sendRobotMessage(subAddress, robotMessage);
    }

    @Override
    public void sendRobotMessage(String subAddress, String message) throws Exception {
        SpringUtils.getBean(this.getClass()).sendMessage(subAddress, new MessageVO(UserCache.getUser(RobotConstant.key),
                message, MessageTypeEnum.ROBOT));
    }
}
