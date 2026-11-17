package com.mat.proexpense_backend.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mat.proexpense_backend.entitties.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

  List<Card> findAllByAccountId(UUID accountId);

  List<Card> findAllByAccountIdAndIsActiveTrue(UUID accountId);
}
