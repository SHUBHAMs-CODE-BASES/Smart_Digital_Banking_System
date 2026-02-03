package com.bank.system.controller;

import com.bank.system.dto.request.BeneficiaryRequest;
import com.bank.system.entity.Beneficiary;
import com.bank.system.security.services.UserDetailsImpl;
import com.bank.system.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping
    public ResponseEntity<List<Beneficiary>> getBeneficiaries(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Beneficiary> beneficiaries = beneficiaryService.getBeneficiaries(userDetails.getId());
        return ResponseEntity.ok(beneficiaries);
    }

    @PostMapping
    public ResponseEntity<Beneficiary> addBeneficiary(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryService.addBeneficiary(userDetails.getId(), request);
        return ResponseEntity.ok(beneficiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.ok().build();
    }
}
