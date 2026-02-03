package com.bank.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.system.entity.Account;
import com.bank.system.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountOrTargetAccountOrderByTimestampDesc(Account sourceAccount,
            Account targetAccount);

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t")
    Double sumTotalTransactionAmount();
}
