package com.et.be.inbox.service;

import com.et.be.inbox.domain.dto.ChatRecordDTO;
import com.et.be.inbox.domain.dto.MsgDTO;

import java.util.HashMap;
import java.util.List;

/**
 * 聊天记录
 *
 * @author yanpanyi
 * @date 2019/4/4
 */
public interface ChatRecordService {

    /**
     * 添加聊天记录
     *
     * @param chatRecordDTO 聊天记录对象
     */
    void addRecord(ChatRecordDTO chatRecordDTO);

    /**
     * 聊天记录列表
     *
     * @param directoryName 目录名
     * @return 聊天记录列表
     */
    List<HashMap<String, Object>> listRecord(String directoryName);

    /**
     * 添加记录
     * @param msgDTO
     */
    void saveMsg(MsgDTO msgDTO);

    /**
     * 消息列表
     * @param msgDTO
     * @return
     */
    List<HashMap<String, Object>> listMsg(MsgDTO msgDTO);

}
