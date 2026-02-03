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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.system.entity.Card;
import com.bank.system.service.CardService;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/my-cards")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Card>> getMyCards(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cardService.getUserCards(userDetails.getUsername()));
    }

    @PostMapping("/issue/{accountId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Card> issueCard(@PathVariable Long accountId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cardService.issueCard(userDetails.getUsername(), accountId));
    }

    @PostMapping("/{cardId}/status")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Card> toggleStatus(@PathVariable Long cardId,
            @RequestParam boolean block,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cardService.toggleBlockStatus(userDetails.getUsername(), cardId, block));
    }
}
