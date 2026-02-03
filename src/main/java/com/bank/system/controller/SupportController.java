package com.bank.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.system.entity.SupportTicket;
import com.bank.system.service.SupportService;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .ok(supportService.createTicket(userDetails.getUsername(), ticket.getSubject(), ticket.getMessage()));
    }

    @GetMapping("/my-tickets")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SupportTicket>> getMyTickets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(supportService.getUserTickets(userDetails.getUsername()));
    }
}
