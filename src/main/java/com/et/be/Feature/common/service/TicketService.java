package com.et.be.Feature.common.service;

import com.et.be.Feature.common.Entity.Ticket;
import org.springframework.stereotype.Service;

//@Service
public interface TicketService {
    Ticket getTicket(String email,String accessCode);
    void saveTicket(String email,String accessCode);
}
