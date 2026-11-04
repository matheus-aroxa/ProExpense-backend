package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.mat.proexpense_backend.dto.request.RegisterExpenseRequest;
import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.repository.ExpenseRepository;

@Service 
public class ExpenseService {
  
  private final ExpenseRepository repository;

  public ExpenseService (ExpenseRepository repository) {
    this.repository = repository;
  }

  public List<Expense> findAllExpenses() {
    return this.repository.findAll();
  }

  public Optional<Expense> findExpenseById(UUID id) {
    return this.repository.findById(id);
  }

  public void registerExpense(RegisterExpenseRequest request) {
    Expense expense = new Expense();
    expense.setName(request.name());
    expense.setDescription(request.description());
    expense.setAmount(request.amount());
    expense.setPaymentMethod(request.paymentMethod());
    expense.setInInstallments(request.isInInstallments());
    expense.setInstallmentsNumber(request.installmentsNumber());

    this.repository.save(expense);
  }
}
