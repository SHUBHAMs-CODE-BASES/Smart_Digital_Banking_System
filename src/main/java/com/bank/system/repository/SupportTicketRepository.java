package com.bank.system.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.system.entity.SupportTicket;
import com.bank.system.entity.User;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUser(User user);
}
