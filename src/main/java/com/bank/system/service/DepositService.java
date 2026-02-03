package com.bank.system.service;

import com.bank.system.dto.request.DepositRequest;
import com.bank.system.entity.Deposit;
import com.bank.system.entity.User;
import com.bank.system.repository.DepositRepository;
import com.bank.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Deposit> getMyDeposits(Long userId) {
        return depositRepository.findByUserId(userId);
    }

    @Transactional
    public Deposit createDeposit(Long userId, DepositRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double interestRate = 6.5; // Default rate
        if ("FD".equalsIgnoreCase(request.getType())) {
            interestRate = 7.1;
        } else if ("RD".equalsIgnoreCase(request.getType())) {
            interestRate = 6.8;
        }

        Deposit deposit = Deposit.builder()
                .type(request.getType())
                .amount(request.getAmount())
                .tenureMonths(request.getTenureMonths())
                .interestRate(interestRate)
                .maturityDate(LocalDate.now().plusMonths(request.getTenureMonths()))
                .user(user)
                .build();

        return depositRepository.save(deposit);
    }
}
