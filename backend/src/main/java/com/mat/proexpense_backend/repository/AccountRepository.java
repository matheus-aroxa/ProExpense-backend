package com.mat.proexpense_backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mat.proexpense_backend.entitties.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

  List<Account> findAllByMembers_User_Id(UUID userId);

  @Query("""
      select count(m) > 0
      from AccountMember m
      where m.account.id = :accountId
        and m.user.id = :userId
        and m.isActive = true
      """)
  boolean existsActiveMember(@Param("accountId") UUID accountId, @Param("userId") UUID userId);
}
