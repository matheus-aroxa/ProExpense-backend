package com.mat.proexpense_backend.entitties;

import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.ColumnDefault;
import com.mat.proexpense_backend.enums.CardPaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "owner_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_card_owner"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private User owner;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "account_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_card_account"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Account account;

  @NotBlank
  @Size(max = 255)
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CardPaymentMethod paymentMethod;

  @PositiveOrZero
  @DecimalMax("9999999999.99")
  @Digits(integer = 10, fraction = 2)
  @Column(name = "card_limit")
  private BigDecimal limit;

  @ColumnDefault("true")
  @Column(nullable = false)
  private boolean isActive = true;
}
