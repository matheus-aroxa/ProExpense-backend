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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
    name = "account_members",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_account_members_account_user",
        columnNames = {"account_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class AccountMember {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "account_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_account_members_account"))
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_account_members_user"))
  private User user;

  @ColumnDefault("true")
  @Column(nullable = false)
  private boolean isActive = true;

  public AccountMember(Account account, User user) {
    this.account = account;
    this.user = user;
  }
}
