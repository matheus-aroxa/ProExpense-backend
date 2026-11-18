package com.mat.proexpense_backend.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.dto.request.RegisterExpenseRequest;
import com.mat.proexpense_backend.dto.request.UpdateExpenseRequest;
import com.mat.proexpense_backend.dto.response.ExpenseResponse;
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
  public ResponseEntity<List<ExpenseResponse>> findAllExpenses() {
    logger.info("finding all expenses");
    return ResponseEntity.ok(this.service.findAllExpenses());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ExpenseResponse> findExpense(@PathVariable UUID id) {
    logger.info("Finding expense by ID: {}", id);
    return ResponseEntity.ok(this.service.findExpenseById(id));
  }

  @PostMapping
  public ResponseEntity<ExpenseResponse> registerExpense(@Valid @RequestBody RegisterExpenseRequest request) {
    logger.info("Registering expense");
    ExpenseResponse response = this.service.registerExpense(request);
    return ResponseEntity.created(URI.create("/api/v1/expenses/" + response.id())).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable UUID id, @Valid @RequestBody UpdateExpenseRequest request) {
    logger.info("Updating expense {}", id);
    return ResponseEntity.ok(this.service.updateExpense(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteExpense(@PathVariable UUID id) {
    logger.info("Deleting expense {}", id);
    this.service.deleteExpense(id);
    return ResponseEntity.noContent().build();
  }

}
