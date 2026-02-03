package com.bank.system.service;

import com.bank.system.dto.request.LoanRequest;
import com.bank.system.entity.Loan;
import com.bank.system.entity.User;
import com.bank.system.repository.LoanRepository;
import com.bank.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Loan> getMyLoans(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    @Transactional
    public Loan applyForLoan(Long userId, LoanRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double interestRate = 10.5; // Default Personal Loan
        if ("Home".equalsIgnoreCase(request.getType())) {
            interestRate = 8.5;
        } else if ("Car".equalsIgnoreCase(request.getType())) {
            interestRate = 9.0;
        }

        Loan loan = Loan.builder()
                .type(request.getType())
                .amount(request.getAmount())
                .interestRate(interestRate)
                .status("PENDING")
                .applicationDate(LocalDate.now())
                .user(user)
                .build();

        return loanRepository.save(loan);
    }
}
