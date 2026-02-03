package com.bank.system.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bank.system.entity.BillBiller;
import com.bank.system.entity.Role;
import com.bank.system.entity.User;
import com.bank.system.repository.BillBillerRepository;
import com.bank.system.repository.RoleRepository;
import com.bank.system.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private com.bank.system.repository.AccountRepository accountRepository;

        @Autowired
        private com.bank.system.repository.TransactionRepository transactionRepository;

        @Autowired
        private BillBillerRepository billBillerRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                // Initialize Roles
                if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                        roleRepository.save(new Role(null, "ROLE_USER"));
                }
                if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                        roleRepository.save(new Role(null, "ROLE_ADMIN"));
                }

                // Initialize Billers
                if (billBillerRepository.count() == 0) {
                        billBillerRepository
                                        .save(BillBiller.builder().name("City Power Co").category("ELECTRICITY")
                                                        .icon("fa-bolt").build());
                        billBillerRepository
                                        .save(BillBiller.builder().name("Metro Water").category("WATER").icon("fa-tint")
                                                        .build());
                        billBillerRepository
                                        .save(BillBiller.builder().name("Speedy Net").category("INTERNET")
                                                        .icon("fa-wifi").build());
                        billBillerRepository
                                        .save(BillBiller.builder().name("Global Mobile").category("PHONE")
                                                        .icon("fa-mobile").build());
                        System.out.println("Billers seeded.");
                }

                // Initialize Admin User and Accounts
                User admin;
                if (!userRepository.existsByUsername("admin")) {
                        Set<Role> roles = new HashSet<>();
                        roles.add(roleRepository.findByName("ROLE_ADMIN").get());
                        roles.add(roleRepository.findByName("ROLE_USER").get());

                        admin = User.builder()
                                        .username("admin")
                                        .email("admin@bank.com")
                                        .password(passwordEncoder.encode("admin123"))
                                        .fullName("System Administrator")
                                        .roles(roles)
                                        .build();
                        userRepository.save(admin);
                        System.out.println("Admin user created: username=admin, password=admin123");
                } else {
                        admin = userRepository.findByUsername("admin").get();
                }

                // Check if admin has any accounts
                if (accountRepository.findByUser(admin).isEmpty()) {

                        // Seed Account for Admin
                        com.bank.system.entity.Account account = com.bank.system.entity.Account.builder()
                                        .accountNumber("1000001234")
                                        .balance(new java.math.BigDecimal("50000.00"))
                                        .status(com.bank.system.entity.AccountStatus.ACTIVE)
                                        .user(admin)
                                        .build();

                        accountRepository.save(account);
                        System.out.println("Seeded Savings Account: 1000001234");

                        // Seed Transaction
                        com.bank.system.entity.Transaction tx1 = com.bank.system.entity.Transaction.builder()
                                        .amount(new java.math.BigDecimal("1500.00"))
                                        .type(com.bank.system.entity.TransactionType.DEPOSIT)
                                        .description("Initial Deposit")
                                        .targetAccount(account)
                                        .timestamp(java.time.LocalDateTime.now().minusDays(2))
                                        .build();
                        transactionRepository.save(tx1);

                        com.bank.system.entity.Transaction tx2 = com.bank.system.entity.Transaction.builder()
                                        .amount(new java.math.BigDecimal("200.00"))
                                        .type(com.bank.system.entity.TransactionType.WITHDRAWAL)
                                        .description("ATM Withdrawal")
                                        .sourceAccount(account)
                                        .timestamp(java.time.LocalDateTime.now().minusDays(1))
                                        .build();
                        transactionRepository.save(tx2);

                        System.out.println("Seeded Transactions");
                }

                // Seed Second User for Transfer Testing
                if (!userRepository.existsByUsername("user1")) {
                        Set<Role> roles = new HashSet<>();
                        roles.add(roleRepository.findByName("ROLE_USER").get());

                        User user1 = User.builder()
                                        .username("user1")
                                        .email("user1@bank.com")
                                        .password(passwordEncoder.encode("user123"))
                                        .fullName("Test User One")
                                        .roles(roles)
                                        .build();
                        userRepository.save(user1);
                        System.out.println("User1 created: username=user1, password=user123");

                        com.bank.system.entity.Account account1 = com.bank.system.entity.Account.builder()
                                        .accountNumber("1000005678")
                                        .balance(new java.math.BigDecimal("10000.00"))
                                        .status(com.bank.system.entity.AccountStatus.ACTIVE)
                                        .user(user1)
                                        .build();
                        accountRepository.save(account1);
                        System.out.println("Seeded Savings Account for User1: 1000005678");
                }
        }
}
