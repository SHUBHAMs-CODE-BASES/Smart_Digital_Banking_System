package com.bank.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.system.entity.Card;
import com.bank.system.entity.User;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUser(User user);

    Optional<Card> findByCardNumber(String cardNumber);
}
