package com.mat.proexpense_backend.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.dto.request.RegisterCardRequest;
import com.mat.proexpense_backend.dto.request.UpdateCardRequest;
import com.mat.proexpense_backend.dto.response.CardResponse;
import com.mat.proexpense_backend.service.CardService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/cards")
public class CardController {

  private static final Logger logger = LoggerFactory.getLogger(CardController.class);

  private final CardService service;

  public CardController(CardService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<CardResponse> findCardById(@PathVariable UUID id) {
    logger.info("finding card by id: {}", id);
    return ResponseEntity.ok(this.service.findCardById(id));
  }

  @GetMapping("/accounts/{accountId}")
  public ResponseEntity<List<CardResponse>> findAllCardsByAccountId(@PathVariable UUID accountId) {
    logger.info("finding all cards by account id: {}", accountId);
    return ResponseEntity.ok(this.service.findAllCardsByAccountId(accountId));
  }

  @GetMapping("/accounts/{accountId}/active")
  public ResponseEntity<List<CardResponse>> findActiveCardsByAccountId(@PathVariable UUID accountId) {
    logger.info("finding active cards by account id: {}", accountId);
    return ResponseEntity.ok(this.service.findActiveCardsByAccountId(accountId));
  }

  @PostMapping
  public ResponseEntity<CardResponse> registerCard(@Valid @RequestBody RegisterCardRequest request) {
    logger.info("registering card");
    CardResponse response = this.service.registerCard(request);
    return ResponseEntity.created(URI.create("/api/v1/cards/" + response.id())).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CardResponse> updateCard(@PathVariable UUID id, @Valid @RequestBody UpdateCardRequest request) {
    logger.info("updating card {}", id);
    return ResponseEntity.ok(this.service.updateCard(id, request));
  }
}
