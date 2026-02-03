package com.bank.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.system.entity.BillBiller;

public interface BillBillerRepository extends JpaRepository<BillBiller, Long> {
}
