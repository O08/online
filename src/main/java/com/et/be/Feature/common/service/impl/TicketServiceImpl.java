package com.et.be.Feature.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.Feature.common.Entity.Ticket;
import com.et.be.Feature.common.constant.TicketConstant;
import com.et.be.Feature.common.dao.TicketMapper;
import com.et.be.Feature.common.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    TicketMapper ticketMapper;
    @Override
    public Ticket getTicket(String email, String accessCode) {
        QueryWrapper<Ticket> wrapper = Wrappers.query();
        wrapper.eq("email",email)
                .eq("access_code",accessCode);

        return ticketMapper.selectOne(wrapper);
    }

    @Override
    public void saveTicket(String email, String accessCode) {
        QueryWrapper<Ticket> wrapper = Wrappers.query();
        wrapper.eq("email",email);
        boolean isNewUser = ticketMapper.selectCount(wrapper) <= 0;
        if(isNewUser){
            Ticket ticket = new Ticket();
            ticket.setEmail(email)
                    .setAccessCode(accessCode)
                    .setCreatedate(new Date())
                    .setModifydate(new Date())
                    .setFlag(TicketConstant.ON_FLAG);
            ticketMapper.insert(ticket);
        }
        if(!isNewUser){
            ticketMapper.updateTicketByEmail(email,accessCode,new Date());
        }
    }
}
