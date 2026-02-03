package com.bank.system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.system.dto.request.BillPaymentRequest;
import com.bank.system.entity.Account;
import com.bank.system.entity.BillBiller;
import com.bank.system.entity.BillPayment;
import com.bank.system.entity.User;
import com.bank.system.repository.AccountRepository;
import com.bank.system.repository.BillBillerRepository;
import com.bank.system.repository.BillPaymentRepository;
import com.bank.system.repository.UserRepository;

@Service
public class BillService {

    @Autowired
    private BillBillerRepository billerRepository;

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BillBiller> getAllBillers() {
        return billerRepository.findAll();
    }

    public List<BillPayment> getMyPaymentHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return billPaymentRepository.findByUser(user);
    }

    @Transactional
    public BillPayment payBill(BillPaymentRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BillBiller biller = billerRepository.findById(request.getBillerId())
                .orElseThrow(() -> new RuntimeException("Biller not found"));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Account does not belong to user");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Deduct balance
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        // Record Payment
        BillPayment payment = BillPayment.builder()
                .user(user)
                .account(account)
                .biller(biller)
                .amount(request.getAmount())
                .referenceNumber(request.getReferenceNumber())
                .status("SUCCESS")
                .paymentDate(LocalDateTime.now())
                .build();

        return billPaymentRepository.save(payment);
    }
}
