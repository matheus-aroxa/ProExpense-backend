package com.mat.proexpense_backend.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.enums.PaymentMethod;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

  boolean existsByCardIdAndPaymentMethod(UUID cardId, PaymentMethod paymentMethod);
}
