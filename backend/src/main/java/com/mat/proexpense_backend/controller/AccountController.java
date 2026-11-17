package com.mat.proexpense_backend.controller;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.dto.request.AddUserToAccountRequest;
import com.mat.proexpense_backend.dto.request.RegisterAccountRequest;
import com.mat.proexpense_backend.dto.response.AccountResponse;
import com.mat.proexpense_backend.service.AccountService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  private final AccountService service;

  public AccountController(AccountService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<AccountResponse> registerAccount(@Valid @RequestBody RegisterAccountRequest request) {
    logger.info("registering account");
    return ResponseEntity.ok(this.service.registerAccount(request));
  }

  @PostMapping("/users")
  public ResponseEntity<AccountResponse> addUserToAccount(@Valid @RequestBody AddUserToAccountRequest request) {
    logger.info("adding user {} to account {}", request.userId(), request.accountId());
    return ResponseEntity.ok(this.service.addUserToAccount(request));
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<List<AccountResponse>> findAllAccountsByUserId(@PathVariable UUID userId) {
    logger.info("finding all accounts by user id: {}", userId);
    return ResponseEntity.ok(this.service.findAllAccountsByUserId(userId));
  }
}
