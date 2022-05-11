package com.et.be.inbox.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.inbox.dao.MsgDao;
import com.et.be.inbox.domain.dto.MsgDTO;
import com.et.be.inbox.domain.mo.Msg;
import com.et.be.inbox.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MsgServiceImpl implements MsgService {
    @Autowired
    private MsgDao msgDao;
    @Override
    public List<Msg> listMsg(MsgDTO msgDTO) {
        QueryWrapper<Msg> wrapper = Wrappers.query();
        wrapper.
                and(wp->wp.eq("sender",msgDTO.getSender())
                        .eq("receiver",msgDTO.getReceiver()))
                .or(wp->wp.eq("sender",msgDTO.getReceiver())
                        .eq("receiver",msgDTO.getSender()));
        return msgDao.selectList(wrapper);
    }

    @Override
    public void addMsg(MsgDTO msgDTO) {
           Msg msg = new Msg();
        BeanUtil.copyProperties(msgDTO,msg);
        msg.setCreatedate(new Date())
                .setModifydate(new Date());

        msgDao.insert(msg);
    }
}
