package com.bank.system.service;

import com.bank.system.dto.request.BeneficiaryRequest;
import com.bank.system.entity.Beneficiary;
import com.bank.system.entity.User;
import com.bank.system.repository.BeneficiaryRepository;
import com.bank.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Beneficiary> getBeneficiaries(Long userId) {
        return beneficiaryRepository.findByUserId(userId);
    }

    @Transactional
    public Beneficiary addBeneficiary(Long userId, BeneficiaryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Beneficiary beneficiary = Beneficiary.builder()
                .name(request.getName())
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .ifscCode(request.getIfscCode())
                .user(user)
                .build();

        return beneficiaryRepository.save(beneficiary);
    }

    @Transactional
    public void deleteBeneficiary(Long id) {
        beneficiaryRepository.deleteById(id);
    }
}
