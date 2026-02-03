package com.bank.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.system.entity.Account;
import com.bank.system.entity.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUser(User user);

    Boolean existsByAccountNumber(String accountNumber);
}
