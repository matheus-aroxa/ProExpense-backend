package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.request.AccountAccessRequest;
import com.mat.proexpense_backend.dto.request.AddUserToAccountRequest;
import com.mat.proexpense_backend.dto.request.RegisterAccountRequest;
import com.mat.proexpense_backend.dto.response.AccountAccessResponse;
import com.mat.proexpense_backend.dto.response.AccountMemberResponse;
import com.mat.proexpense_backend.dto.response.AccountResponse;
import com.mat.proexpense_backend.entitties.Account;
import com.mat.proexpense_backend.entitties.AccountMember;
import com.mat.proexpense_backend.entitties.User;
import com.mat.proexpense_backend.repository.AccountRepository;
import com.mat.proexpense_backend.repository.UserRepository;

@Service
public class AccountService {

  private final AccountRepository repository;
  private final UserRepository userRepository;

  public AccountService(AccountRepository repository, UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  @Transactional
  public AccountResponse registerAccount(RegisterAccountRequest request) {
    User owner = this.findUser(request.ownerId());

    Account account = new Account();
    account.setName(request.name());
    account.setOwner(owner);
    account.addMember(owner);

    return AccountResponse.from(this.repository.save(account));
  }

  @Transactional
  public AccountResponse addUserToAccount(AddUserToAccountRequest request) {
    Account account = this.findAccount(request.accountId());
    User user = this.findUser(request.userId());

    if (account.findMember(user.getId()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já faz parte da conta");
    }

    account.addMember(user);
    return AccountResponse.from(this.repository.save(account));
  }

  @Transactional
  public AccountResponse activateMember(UUID accountId, UUID userId) {
    return this.setMemberActive(accountId, userId, true);
  }

  @Transactional
  public AccountResponse deactivateMember(UUID accountId, UUID userId) {
    Account account = this.findAccount(accountId);
    if (account.getOwner().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "O dono da conta não pode ser desativado");
    }
    return this.setMemberActive(accountId, userId, false);
  }

  @Transactional(readOnly = true)
  public List<AccountMemberResponse> findActiveMembers(UUID accountId) {
    return this.findAccount(accountId).getMembers().stream()
        .filter(AccountMember::isActive)
        .map(AccountMemberResponse::from)
        .toList();
  }

  private AccountResponse setMemberActive(UUID accountId, UUID userId, boolean active) {
    Account account = this.findAccount(accountId);
    AccountMember member = account.findMember(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não faz parte da conta"));

    member.setActive(active);
    return AccountResponse.from(this.repository.save(account));
  }

  @Transactional(readOnly = true)
  public AccountAccessResponse hasAccessToTheAccount(AccountAccessRequest request) {
    boolean hasAccess = this.repository.existsActiveMember(request.accountId(), request.userId());
    return new AccountAccessResponse(request.userId(), request.accountId(), hasAccess);
  }

  @Transactional(readOnly = true)
  public List<AccountResponse> findAllAccountsByUserId(UUID userId) {
    return this.repository.findAllByMembers_User_Id(userId).stream()
        .map(AccountResponse::from)
        .toList();
  }

  private Account findAccount(UUID id) {
    return this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
  }

  private User findUser(UUID id) {
    return this.userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
  }
}
