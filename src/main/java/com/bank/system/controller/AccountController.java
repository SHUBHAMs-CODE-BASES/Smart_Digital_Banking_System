package com.bank.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.system.dto.request.CreateAccountRequest;
import com.bank.system.entity.Account;
import com.bank.system.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Account account = accountService.createAccount(request, userDetails.getUsername());
        return ResponseEntity.ok(account);
    }

    @GetMapping("/my-accounts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Account>> getMyAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        List<Account> accounts = accountService.getUserAccounts(userDetails.getUsername());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        // In a real app, verify that the account belongs to the user or user is admin
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }
}
