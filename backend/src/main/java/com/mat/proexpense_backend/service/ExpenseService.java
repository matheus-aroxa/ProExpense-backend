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
import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.repository.ExpenseRepository;

@Service
public class ExpenseService {

  private final ExpenseRepository repository;
  private final AccountService accountService;

  public ExpenseService(ExpenseRepository repository, AccountService accountService) {
    this.repository = repository;
    this.accountService = accountService;
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

    Expense expense = new Expense();
    expense.setOwnerId(request.ownerId());
    expense.setAccountId(request.accountId());
    expense.setName(request.name());
    expense.setDescription(request.description());
    expense.setAmount(request.amount());
    expense.setPaymentMethod(request.paymentMethod());
    expense.setInInstallments(request.isInInstallments());
    expense.setInstallmentsNumber(request.installmentsNumber());

    return ExpenseResponse.from(this.repository.save(expense));
  }
}
