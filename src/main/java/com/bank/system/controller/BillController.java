package com.bank.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.system.dto.request.BillPaymentRequest;
import com.bank.system.entity.BillBiller;
import com.bank.system.entity.BillPayment;
import com.bank.system.service.BillService;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/billers")
    public ResponseEntity<List<BillBiller>> getBillers() {
        return ResponseEntity.ok(billService.getAllBillers());
    }

    @PostMapping("/pay")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BillPayment> payBill(@RequestBody BillPaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(billService.payBill(request, userDetails.getUsername()));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BillPayment>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(billService.getMyPaymentHistory(userDetails.getUsername()));
    }
}
