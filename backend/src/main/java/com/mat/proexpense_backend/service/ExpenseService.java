package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.request.AccountAccessRequest;
import com.mat.proexpense_backend.dto.request.RegisterExpenseRequest;
import com.mat.proexpense_backend.dto.response.ExpenseResponse;
import com.mat.proexpense_backend.entitties.Card;
import com.mat.proexpense_backend.entitties.Category;
import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.repository.AccountRepository;
import com.mat.proexpense_backend.repository.CardRepository;
import com.mat.proexpense_backend.repository.CategoryRepository;
import com.mat.proexpense_backend.repository.ExpenseRepository;
import com.mat.proexpense_backend.repository.UserRepository;

@Service
public class ExpenseService {

  private final ExpenseRepository repository;
  private final AccountService accountService;
  private final CategoryRepository categoryRepository;
  private final CardRepository cardRepository;
  private final UserRepository userRepository;
  private final AccountRepository accountRepository;

  public ExpenseService(
      ExpenseRepository repository,
      AccountService accountService,
      CategoryRepository categoryRepository,
      CardRepository cardRepository,
      UserRepository userRepository,
      AccountRepository accountRepository) {
    this.repository = repository;
    this.accountService = accountService;
    this.categoryRepository = categoryRepository;
    this.cardRepository = cardRepository;
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;
  }

  public List<ExpenseResponse> findAllExpenses() {
    return this.repository.findAll().stream()
        .map(ExpenseResponse::from)
        .toList();
  }

  public ExpenseResponse findExpenseById(UUID id) {
    return this.repository.findById(id)
        .map(ExpenseResponse::from)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Despesa não encontrada"));
  }

  @Transactional
  public ExpenseResponse registerExpense(RegisterExpenseRequest request) {
    AccountAccessRequest accessRequest = new AccountAccessRequest(request.ownerId(), request.accountId());
    if (!this.accountService.hasAccessToTheAccount(accessRequest).hasAccess()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem acesso à conta");
    }

    Category category = null;
    if (request.categoryId() != null) {
      category = this.categoryRepository.findById(request.categoryId())
          .filter(found -> found.getAccount().getId().equals(request.accountId()))
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não encontrada na conta"));

      if (!category.isActive()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria está inativa");
      }
    }

    Card card = this.cardRepository.findById(request.cardId())
        .filter(found -> found.getAccount().getId().equals(request.accountId()))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cartão não encontrado na conta"));

    if (!card.isActive()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cartão está inativo");
    }

    if (!card.getPaymentMethod().supports(request.paymentMethod())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forma de pagamento não suportada pelo cartão");
    }

    Expense expense = new Expense();
    expense.setOwner(this.userRepository.getReferenceById(request.ownerId()));
    expense.setAccount(this.accountRepository.getReferenceById(request.accountId()));
    expense.setCategory(category);
    expense.setCard(card);
    expense.setName(request.name());
    expense.setDescription(request.description());
    expense.setAmount(request.amount());
    expense.setPaymentMethod(request.paymentMethod());
    expense.setInInstallments(request.isInInstallments());
    expense.setInstallmentsNumber(request.installmentsNumber());

    return ExpenseResponse.from(this.repository.save(expense));
  }
}
