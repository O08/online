package com.et.be.inbox.service;

import com.et.be.inbox.domain.dto.MsgDTO;
import com.et.be.inbox.domain.mo.Msg;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MsgService {
    List<Msg> listMsg(MsgDTO msgDTO);
    void addMsg(MsgDTO msgDTO);
}
