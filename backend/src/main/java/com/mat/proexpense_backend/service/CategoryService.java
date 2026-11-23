package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.request.RegisterCategoryRequest;
import com.mat.proexpense_backend.dto.request.UpdateCategoryRequest;
import com.mat.proexpense_backend.dto.response.CategoryResponse;
import com.mat.proexpense_backend.entitties.Category;
import com.mat.proexpense_backend.repository.AccountRepository;
import com.mat.proexpense_backend.repository.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository repository;
  private final AccountRepository accountRepository;

  public CategoryService(CategoryRepository repository, AccountRepository accountRepository) {
    this.repository = repository;
    this.accountRepository = accountRepository;
  }

  @Transactional(readOnly = true)
  public CategoryResponse findCategoryById(UUID id) {
    return CategoryResponse.from(this.findCategory(id));
  }

  @Transactional(readOnly = true)
  public List<CategoryResponse> findAllCategoriesByAccountId(UUID accountId) {
    return this.repository.findAllByAccountId(accountId).stream()
        .map(CategoryResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<CategoryResponse> findActiveCategoriesByAccountId(UUID accountId) {
    return this.repository.findAllByAccountIdAndIsActiveTrue(accountId).stream()
        .map(CategoryResponse::from)
        .toList();
  }

  @Transactional
  public CategoryResponse registerCategory(RegisterCategoryRequest request) {
    if (!this.accountRepository.existsById(request.accountId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
    }

    this.ensureNameIsAvailable(request.accountId(), request.name(), null);

    Category category = new Category();
    category.setAccount(this.accountRepository.getReferenceById(request.accountId()));
    category.setName(request.name());

    return CategoryResponse.from(this.repository.save(category));
  }

  @Transactional
  public CategoryResponse updateCategory(UUID id, UpdateCategoryRequest request) {
    Category category = this.findCategory(id);

    this.ensureNameIsAvailable(category.getAccount().getId(), request.name(), id);

    category.setName(request.name());
    category.setActive(request.isActive());

    return CategoryResponse.from(this.repository.save(category));
  }

  private void ensureNameIsAvailable(UUID accountId, String name, UUID ignoredCategoryId) {
    boolean nameAlreadyExists = this.repository.findAllByAccountId(accountId).stream()
        .filter(category -> !category.getId().equals(ignoredCategoryId))
        .anyMatch(category -> category.getName().equalsIgnoreCase(name));
    if (nameAlreadyExists) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "A category with this name already exists in the account");
    }
  }

  private Category findCategory(UUID id) {
    return this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
  }
}
