package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.request.AddUserToAccountRequest;
import com.mat.proexpense_backend.dto.request.RegisterAccountRequest;
import com.mat.proexpense_backend.dto.response.AccountResponse;
import com.mat.proexpense_backend.entitties.Account;
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
    account.getUsers().add(owner);

    return AccountResponse.from(this.repository.save(account));
  }

  @Transactional
  public AccountResponse addUserToAccount(AddUserToAccountRequest request) {
    Account account = this.repository.findById(request.accountId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    User user = this.findUser(request.userId());

    if (!account.getUsers().add(user)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já faz parte da conta");
    }

    return AccountResponse.from(this.repository.save(account));
  }

  @Transactional(readOnly = true)
  public List<AccountResponse> findAllAccountsByUserId(UUID userId) {
    return this.repository.findAllByUsers_Id(userId).stream()
        .map(AccountResponse::from)
        .toList();
  }

  private User findUser(UUID id) {
    return this.userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
  }
}
