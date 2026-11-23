package com.mat.proexpense_backend.controller.docs;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.mat.proexpense_backend.dto.request.RegisterExpenseRequest;
import com.mat.proexpense_backend.dto.request.UpdateExpenseRequest;
import com.mat.proexpense_backend.dto.response.ExpenseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Expenses", description = "Expenses and installment purchases")
public interface ExpenseControllerDocs {

  @Operation(summary = "List all expenses")
  @ApiResponse(responseCode = "200", description = "Expenses found")
  ResponseEntity<List<ExpenseResponse>> findAllExpenses();

  @Operation(summary = "Find expense by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Expense found"),
      @ApiResponse(responseCode = "404", description = "Expense not found")
  })
  ResponseEntity<ExpenseResponse> findExpense(@Parameter(description = "Expense ID") UUID id);

  @Operation(
      summary = "Register expense",
      description = """
          Registers a single-payment or an installment expense. The owner must be an active member of the account, \
          and the card and category must be active and belong to the same account.

          **Single payment:** `isInInstallments = false`, `installmentsNumber = 0` and no recurrence fields.

          **Installments:** only for `CREDIT`, with 2 to 120 installments. One expense is created per installment \
          and the first one is returned. The amount is split evenly, with any remainder added to the first installment. \
          Installment dates follow either `recurrenceDayOfMonth` (fixed day of the month) or \
          `recurrenceUnit` + `recurrenceInterval` (defaults to every 1 month), never both.""")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Expense registered"),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid request body, card or category not found in the account or inactive, "
              + "payment method not supported by the card, installments not on credit, "
              + "or amount insufficient for the number of installments"),
      @ApiResponse(
          responseCode = "403",
          description = "Missing or invalid authentication token, or owner does not have access to the account")
  })
  ResponseEntity<ExpenseResponse> registerExpense(RegisterExpenseRequest request);

  @Operation(
      summary = "Update expense",
      description = "Updates a single expense. For installment purchases, only the given installment is changed")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Expense updated"),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid request body, card or category not found in the account or inactive, "
              + "payment method not supported by the card, or installments not on credit"),
      @ApiResponse(responseCode = "404", description = "Expense not found")
  })
  ResponseEntity<ExpenseResponse> updateExpense(@Parameter(description = "Expense ID") UUID id, UpdateExpenseRequest request);

  @Operation(summary = "Delete expense", description = "For installment purchases, only the given installment is deleted")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Expense deleted"),
      @ApiResponse(responseCode = "404", description = "Expense not found")
  })
  ResponseEntity<Void> deleteExpense(@Parameter(description = "Expense ID") UUID id);
}
