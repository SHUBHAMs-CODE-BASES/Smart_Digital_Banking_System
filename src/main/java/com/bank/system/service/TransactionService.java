package com.bank.system.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.system.dto.request.TransactionRequest;
import com.bank.system.entity.Account;
import com.bank.system.entity.Transaction;
import com.bank.system.entity.TransactionType;
import com.bank.system.repository.AccountRepository;
import com.bank.system.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction performTransaction(TransactionRequest request) {
        String typeStr = request.getType().toUpperCase();
        TransactionType type;
        try {
            type = TransactionType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid transaction type");
        }

        switch (type) {
            case DEPOSIT:
                return deposit(request.getTargetAccountNumber(), request.getAmount(), request.getDescription());
            case WITHDRAWAL:
                return withdraw(request.getSourceAccountNumber(), request.getAmount(), request.getDescription());
            case TRANSFER:
                return transfer(request.getSourceAccountNumber(), request.getTargetAccountNumber(), request.getAmount(),
                        request.getDescription());
            default:
                throw new RuntimeException("Unsupported transaction type");
        }
    }

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .description(description)
                .targetAccount(account)
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount, String description) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.WITHDRAWAL)
                .description(description)
                .sourceAccount(account)
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transfer(String fromAccountNum, String toAccountNum, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNum)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNum)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .description(description)
                .sourceAccount(fromAccount)
                .targetAccount(toAccount)
                .build();

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        // This query finds all transactions where account is source OR target
        // But JpaRepository method definitions for OR are tricky with multiple params
        // if not careful.
        // Let's rely on the custom method we defined in Repository:
        // findBySourceAccountOrTargetAccountOrderByTimestampDesc
        return transactionRepository.findBySourceAccountOrTargetAccountOrderByTimestampDesc(account, account);
    }
}
