package com.mat.proexpense_backend.entitties;

import java.math.BigDecimal;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mat.proexpense_backend.enums.PaymentMethod;
import com.mat.proexpense_backend.validation.InstallmentPlan;
import com.mat.proexpense_backend.validation.ValidInstallments;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
@ValidInstallments
public class Expense implements InstallmentPlan {
  
  @Id 
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "owner_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_expense_owner"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private User owner;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "account_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_expense_account"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "category_id",
      foreignKey = @ForeignKey(name = "fk_expense_category"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Category category;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "card_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_expense_card"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Card card;

  @NotBlank
  @Size(max = 255)
  private String name;

  @Size(max = 255)
  private String description;

  @NotNull
  @Positive
  @DecimalMax("9999999999.99")
  @Digits(integer = 10, fraction = 2)
  private BigDecimal amount;

  @NotNull
  private PaymentMethod paymentMethod;

  private boolean isInInstallments;

  private int installmentsNumber;

  @Override
  @JsonProperty("isInInstallments")
  public boolean isInInstallments() {
    return this.isInInstallments;
  }

  @Override
  public int installmentsNumber() {
    return this.installmentsNumber;
  }
}
