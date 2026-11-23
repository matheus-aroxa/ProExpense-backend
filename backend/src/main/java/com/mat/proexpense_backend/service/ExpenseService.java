package com.mat.proexpense_backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.request.AccountAccessRequest;
import com.mat.proexpense_backend.dto.request.RegisterExpenseRequest;
import com.mat.proexpense_backend.dto.request.UpdateExpenseRequest;
import com.mat.proexpense_backend.dto.response.ExpenseResponse;
import com.mat.proexpense_backend.entitties.Card;
import com.mat.proexpense_backend.entitties.Category;
import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.enums.PaymentMethod;
import com.mat.proexpense_backend.enums.RecurrenceUnit;
import com.mat.proexpense_backend.repository.AccountRepository;
import com.mat.proexpense_backend.repository.CardRepository;
import com.mat.proexpense_backend.repository.CategoryRepository;
import com.mat.proexpense_backend.repository.ExpenseRepository;
import com.mat.proexpense_backend.repository.UserRepository;

@Service
public class ExpenseService {

  private static final RecurrenceUnit DEFAULT_RECURRENCE_UNIT = RecurrenceUnit.MONTHS;
  private static final int DEFAULT_RECURRENCE_INTERVAL = 1;

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
    return ExpenseResponse.from(this.findExpense(id));
  }

  @Transactional
  public ExpenseResponse registerExpense(RegisterExpenseRequest request) {
    this.ensureInstallmentsAreOnCredit(request.isInInstallments(), request.paymentMethod());

    AccountAccessRequest accessRequest = new AccountAccessRequest(request.ownerId(), request.accountId());
    if (!this.accountService.hasAccessToTheAccount(accessRequest).hasAccess()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have access to the account");
    }

    Category category = this.resolveCategory(request.categoryId(), request.accountId(), null);
    Card card = this.resolveCard(request.cardId(), request.accountId(), null);
    this.ensureCardSupports(card, request.paymentMethod());

    if (!request.isInInstallments()) {
      Expense expense = this.newExpense(request, category, card, request.amount(), request.expenseDate());
      return ExpenseResponse.from(this.repository.save(expense));
    }

    int installmentsNumber = request.installmentsNumber();
    List<BigDecimal> amounts = this.splitAmount(request.amount(), installmentsNumber);
    List<LocalDate> dates = this.installmentDates(request);

    List<Expense> installments = new ArrayList<>();
    for (int index = 0; index < installmentsNumber; index++) {
      Expense installment = this.newExpense(request, category, card, amounts.get(index), dates.get(index));
      installment.setCurrentInstallment(index + 1);
      installments.add(installment);
    }

    this.repository.saveAll(installments);
    return ExpenseResponse.from(installments.getFirst());
  }

  @Transactional
  public ExpenseResponse updateExpense(UUID id, UpdateExpenseRequest request) {
    Expense expense = this.findExpense(id);
    this.ensureInstallmentsAreOnCredit(expense.isInInstallments(), request.paymentMethod());

    UUID accountId = expense.getAccount().getId();
    Category category = this.resolveCategory(request.categoryId(), accountId, expense.getCategory());
    Card card = this.resolveCard(request.cardId(), accountId, expense.getCard());
    this.ensureCardSupports(card, request.paymentMethod());

    expense.setName(request.name());
    expense.setDescription(request.description());
    expense.setCategory(category);
    expense.setCard(card);
    expense.setPaymentMethod(request.paymentMethod());
    expense.setAmount(request.amount());
    expense.setExpenseDate(request.expenseDate());

    return ExpenseResponse.from(this.repository.save(expense));
  }

  @Transactional
  public void deleteExpense(UUID id) {
    this.repository.delete(this.findExpense(id));
  }

  private void ensureInstallmentsAreOnCredit(boolean isInInstallments, PaymentMethod paymentMethod) {
    if (isInInstallments && paymentMethod != PaymentMethod.CREDIT) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only credit purchases can be paid in installments");
    }
  }

  private Category resolveCategory(UUID categoryId, UUID accountId, Category current) {
    if (categoryId == null) {
      return null;
    }

    if (current != null && current.getId().equals(categoryId)) {
      return current;
    }

    Category category = this.categoryRepository.findById(categoryId)
        .filter(found -> found.getAccount().getId().equals(accountId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found in the account"));

    if (!category.isActive()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category is inactive");
    }

    return category;
  }

  private Card resolveCard(UUID cardId, UUID accountId, Card current) {
    if (current != null && current.getId().equals(cardId)) {
      return current;
    }

    Card card = this.cardRepository.findById(cardId)
        .filter(found -> found.getAccount().getId().equals(accountId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card not found in the account"));

    if (!card.isActive()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is inactive");
    }

    return card;
  }

  private void ensureCardSupports(Card card, PaymentMethod paymentMethod) {
    if (!card.getPaymentMethod().supports(paymentMethod)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method not supported by the card");
    }
  }

  private Expense findExpense(UUID id) {
    return this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
  }

  private Expense newExpense(
      RegisterExpenseRequest request,
      Category category,
      Card card,
      BigDecimal amount,
      LocalDate expenseDate) {
    Expense expense = new Expense();
    expense.setOwner(this.userRepository.getReferenceById(request.ownerId()));
    expense.setAccount(this.accountRepository.getReferenceById(request.accountId()));
    expense.setCategory(category);
    expense.setCard(card);
    expense.setName(request.name());
    expense.setDescription(request.description());
    expense.setAmount(amount);
    expense.setPaymentMethod(request.paymentMethod());
    expense.setExpenseDate(expenseDate);
    expense.setInInstallments(request.isInInstallments());
    expense.setInstallmentsNumber(request.installmentsNumber());
    return expense;
  }

  private List<BigDecimal> splitAmount(BigDecimal total, int installmentsNumber) {
    BigDecimal installments = BigDecimal.valueOf(installmentsNumber);
    BigDecimal base = total.divide(installments, 2, RoundingMode.DOWN);
    if (base.signum() == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount is insufficient for the number of installments");
    }

    BigDecimal remainder = total.subtract(base.multiply(installments));
    List<BigDecimal> amounts = new ArrayList<>(Collections.nCopies(installmentsNumber, base));
    amounts.set(0, base.add(remainder));
    return amounts;
  }

  private List<LocalDate> installmentDates(RegisterExpenseRequest request) {
    if (request.recurrenceDayOfMonth() != null) {
      return InstallmentScheduler.byDayOfMonth(
          request.expenseDate(), request.installmentsNumber(), request.recurrenceDayOfMonth());
    }

    RecurrenceUnit unit = request.recurrenceUnit() != null ? request.recurrenceUnit() : DEFAULT_RECURRENCE_UNIT;
    int interval = request.recurrenceInterval() != null ? request.recurrenceInterval() : DEFAULT_RECURRENCE_INTERVAL;
    return InstallmentScheduler.byInterval(request.expenseDate(), request.installmentsNumber(), unit, interval);
  }
}
