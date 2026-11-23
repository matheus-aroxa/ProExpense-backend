package com.mat.proexpense_backend.controller;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.controller.docs.AccountControllerDocs;
import com.mat.proexpense_backend.dto.request.AccountAccessRequest;
import com.mat.proexpense_backend.dto.request.AddUserToAccountRequest;
import com.mat.proexpense_backend.dto.request.RegisterAccountRequest;
import com.mat.proexpense_backend.dto.response.AccountAccessResponse;
import com.mat.proexpense_backend.dto.response.AccountMemberResponse;
import com.mat.proexpense_backend.dto.response.AccountResponse;
import com.mat.proexpense_backend.service.AccountService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController implements AccountControllerDocs {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  private final AccountService service;

  public AccountController(AccountService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  public ResponseEntity<AccountResponse> registerAccount(@Valid @RequestBody RegisterAccountRequest request) {
    logger.info("registering account");
    return ResponseEntity.ok(this.service.registerAccount(request));
  }

  @Override
  @PostMapping("/users")
  public ResponseEntity<AccountResponse> addUserToAccount(@Valid @RequestBody AddUserToAccountRequest request) {
    logger.info("adding user {} to account {}", request.userId(), request.accountId());
    return ResponseEntity.ok(this.service.addUserToAccount(request));
  }

  @Override
  @GetMapping("/{accountId}/users/active")
  public ResponseEntity<List<AccountMemberResponse>> findActiveMembers(@PathVariable UUID accountId) {
    logger.info("finding active members of account {}", accountId);
    return ResponseEntity.ok(this.service.findActiveMembers(accountId));
  }

  @Override
  @PatchMapping("/{accountId}/users/{userId}/activate")
  public ResponseEntity<AccountResponse> activateMember(@PathVariable UUID accountId, @PathVariable UUID userId) {
    logger.info("activating user {} in account {}", userId, accountId);
    return ResponseEntity.ok(this.service.activateMember(accountId, userId));
  }

  @Override
  @PatchMapping("/{accountId}/users/{userId}/deactivate")
  public ResponseEntity<AccountResponse> deactivateMember(@PathVariable UUID accountId, @PathVariable UUID userId) {
    logger.info("deactivating user {} in account {}", userId, accountId);
    return ResponseEntity.ok(this.service.deactivateMember(accountId, userId));
  }

  @Override
  @GetMapping("/access")
  public ResponseEntity<AccountAccessResponse> hasAccessToTheAccount(@Valid @ModelAttribute AccountAccessRequest request) {
    logger.info("checking access of user {} to account {}", request.userId(), request.accountId());
    return ResponseEntity.ok(this.service.hasAccessToTheAccount(request));
  }

  @Override
  @GetMapping("/users/{userId}")
  public ResponseEntity<List<AccountResponse>> findAllAccountsByUserId(@PathVariable UUID userId) {
    logger.info("finding all accounts by user id: {}", userId);
    return ResponseEntity.ok(this.service.findAllAccountsByUserId(userId));
  }
}
