package com.bank.system.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.system.entity.BillPayment;
import com.bank.system.entity.User;

public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
    List<BillPayment> findByUser(User user);
}
