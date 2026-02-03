package com.bank.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.system.entity.Account;
import com.bank.system.entity.Card;
import com.bank.system.entity.User;
import com.bank.system.repository.AccountRepository;
import com.bank.system.repository.CardRepository;
import com.bank.system.repository.UserRepository;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Card> getUserCards(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cardRepository.findByUser(user);
    }

    public Card issueCard(String username, Long accountId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Account does not belong to user");
        }

        Card card = Card.builder()
                .user(user)
                .account(account)
                .cardNumber(generateCardNumber())
                .cardHolderName(user.getFullName())
                .expiryDate("12/28") // Static for now, or calculate
                .cvv(String.format("%03d", new Random().nextInt(999)))
                .pin("1234")
                .type("DEBIT")
                .status("ACTIVE")
                .limitAmount(new BigDecimal("5000.00"))
                .build();

        return cardRepository.save(card);
    }

    public Card toggleBlockStatus(String username, Long cardId, boolean block) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!card.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        card.setStatus(block ? "BLOCKED" : "ACTIVE");
        return cardRepository.save(card);
    }

    private String generateCardNumber() {
        // Simple 16 digit generation
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(String.format("%04d", random.nextInt(10000)));
            if (i < 3)
                sb.append("-");
        }
        return sb.toString();
    }
}
