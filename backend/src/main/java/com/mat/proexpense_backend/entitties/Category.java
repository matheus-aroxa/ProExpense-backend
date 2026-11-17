package com.mat.proexpense_backend.entitties;

import java.util.UUID;
import org.hibernate.annotations.ColumnDefault;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "category",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_category_account_name",
        columnNames = {"account_id", "name"}))
@Data
@RequiredArgsConstructor
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "account_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_category_account"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Account account;

  @NotBlank
  @Size(max = 255)
  @Column(nullable = false)
  private String name;

  @ColumnDefault("true")
  @Column(nullable = false)
  private boolean isActive = true;
}
