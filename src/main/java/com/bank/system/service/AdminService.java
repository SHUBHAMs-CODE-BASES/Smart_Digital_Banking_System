package com.bank.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.system.entity.Account;
import com.bank.system.entity.AccountStatus;
import com.bank.system.entity.Transaction;
import com.bank.system.entity.User;
import com.bank.system.entity.UserStatus;
import com.bank.system.repository.AccountRepository;
import com.bank.system.repository.TransactionRepository;
import com.bank.system.repository.UserRepository;
import com.bank.system.dto.response.AdminDashboardDTO;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public AdminDashboardDTO getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalAccounts = accountRepository.count();
        long totalTransactions = transactionRepository.count();
        Double volume = transactionRepository.sumTotalTransactionAmount();
        java.math.BigDecimal totalVolume = (volume != null) ? java.math.BigDecimal.valueOf(volume)
                : java.math.BigDecimal.ZERO;

        return AdminDashboardDTO.builder()
                .totalUsers(totalUsers)
                .totalAccounts(totalAccounts)
                .totalTransactions(totalTransactions)
                .totalTransactionAmount(totalVolume)
                .build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public com.bank.system.entity.User updateUserStatus(Long userId, String statusStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            UserStatus status = UserStatus.valueOf(statusStr.toUpperCase());
            user.setStatus(status);
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + statusStr);
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Account updateAccountStatus(String accountNumber, String statusStr) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        try {
            AccountStatus status = AccountStatus.valueOf(statusStr.toUpperCase());
            account.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid account status");
        }

        return accountRepository.save(account);
    }
}
