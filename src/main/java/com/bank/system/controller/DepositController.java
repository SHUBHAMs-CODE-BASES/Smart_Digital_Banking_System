package com.bank.system.controller;

import com.bank.system.dto.request.DepositRequest;
import com.bank.system.entity.Deposit;
import com.bank.system.security.services.UserDetailsImpl;
import com.bank.system.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deposits")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DepositController {

    @Autowired
    private DepositService depositService;

    @GetMapping("/my-deposits")
    public ResponseEntity<List<Deposit>> getMyDeposits(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(depositService.getMyDeposits(userDetails.getId()));
    }

    @PostMapping("/create")
    public ResponseEntity<Deposit> createDeposit(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody DepositRequest request) {
        return ResponseEntity.ok(depositService.createDeposit(userDetails.getId(), request));
    }
}
