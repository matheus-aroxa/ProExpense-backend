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
import com.mat.proexpense_backend.dto.request.RegisterExpenseRequest;
import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.service.ExpenseService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseController {

  private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
  private final ExpenseService service;

  public ExpenseController(ExpenseService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<Expense>> findAllExpenses() {
    logger.info("findind all expenses");
    return ResponseEntity.ok(this.service.findAllExpenses());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Expense> findExpense(@PathVariable UUID id) {
    logger.info("Finding expense by ID: {}", id);
    return ResponseEntity.of(this.service.findExpenseById(id));
  }

  @PostMapping
  public ResponseEntity<String> registerExpense(@Valid @RequestBody RegisterExpenseRequest request) {
    logger.info("Registering expense");
    this.service.registerExpense(request);
    return ResponseEntity.ok("ok");
  }

}
