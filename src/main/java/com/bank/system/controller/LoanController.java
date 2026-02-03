package com.bank.system.controller;

import com.bank.system.dto.request.LoanRequest;
import com.bank.system.entity.Loan;
import com.bank.system.security.services.UserDetailsImpl;
import com.bank.system.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/my-loans")
    public ResponseEntity<List<Loan>> getMyLoans(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(loanService.getMyLoans(userDetails.getId()));
    }

    @PostMapping("/apply")
    public ResponseEntity<Loan> applyForLoan(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.applyForLoan(userDetails.getId(), request));
    }
}
