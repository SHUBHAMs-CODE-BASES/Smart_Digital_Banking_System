package com.bank.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.system.entity.SupportTicket;
import com.bank.system.entity.User;
import com.bank.system.repository.SupportTicketRepository;
import com.bank.system.repository.UserRepository;

@Service
public class SupportService {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private UserRepository userRepository;

    public SupportTicket createTicket(String username, String subject, String message) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .subject(subject)
                .message(message)
                .status("OPEN")
                .build();

        return supportTicketRepository.save(ticket);
    }

    public List<SupportTicket> getUserTickets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return supportTicketRepository.findByUser(user);
    }
}
