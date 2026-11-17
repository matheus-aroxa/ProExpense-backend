package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.request.AccountAccessRequest;
import com.mat.proexpense_backend.dto.request.RegisterCardRequest;
import com.mat.proexpense_backend.dto.request.UpdateCardRequest;
import com.mat.proexpense_backend.dto.response.CardResponse;
import com.mat.proexpense_backend.entitties.Card;
import com.mat.proexpense_backend.enums.PaymentMethod;
import com.mat.proexpense_backend.repository.AccountRepository;
import com.mat.proexpense_backend.repository.CardRepository;
import com.mat.proexpense_backend.repository.ExpenseRepository;
import com.mat.proexpense_backend.repository.UserRepository;

@Service
public class CardService {

  private final CardRepository repository;
  private final AccountService accountService;
  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final ExpenseRepository expenseRepository;

  public CardService(
      CardRepository repository,
      AccountService accountService,
      UserRepository userRepository,
      AccountRepository accountRepository,
      ExpenseRepository expenseRepository) {
    this.repository = repository;
    this.accountService = accountService;
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;
    this.expenseRepository = expenseRepository;
  }

  @Transactional(readOnly = true)
  public CardResponse findCardById(UUID id) {
    return CardResponse.from(this.findCard(id));
  }

  @Transactional(readOnly = true)
  public List<CardResponse> findAllCardsByAccountId(UUID accountId) {
    return this.repository.findAllByAccountId(accountId).stream()
        .map(CardResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<CardResponse> findActiveCardsByAccountId(UUID accountId) {
    return this.repository.findAllByAccountIdAndIsActiveTrue(accountId).stream()
        .map(CardResponse::from)
        .toList();
  }

  @Transactional
  public CardResponse registerCard(RegisterCardRequest request) {
    AccountAccessRequest accessRequest = new AccountAccessRequest(request.ownerId(), request.accountId());
    if (!this.accountService.hasAccessToTheAccount(accessRequest).hasAccess()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem acesso à conta");
    }

    Card card = new Card();
    card.setOwner(this.userRepository.getReferenceById(request.ownerId()));
    card.setAccount(this.accountRepository.getReferenceById(request.accountId()));
    card.setName(request.name());
    card.setPaymentMethod(request.paymentMethod());
    card.setLimit(request.limit());

    return CardResponse.from(this.repository.save(card));
  }

  @Transactional
  public CardResponse updateCard(UUID id, UpdateCardRequest request) {
    Card card = this.findCard(id);

    for (PaymentMethod method : PaymentMethod.values()) {
      if (!request.paymentMethod().supports(method)
          && this.expenseRepository.existsByCardIdAndPaymentMethod(id, method)) {
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "O cartão possui despesas com uma forma de pagamento que não seria mais suportada");
      }
    }

    card.setName(request.name());
    card.setPaymentMethod(request.paymentMethod());
    card.setLimit(request.limit());
    card.setActive(request.isActive());

    return CardResponse.from(this.repository.save(card));
  }

  private Card findCard(UUID id) {
    return this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado"));
  }
}
