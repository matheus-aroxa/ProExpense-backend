package com.mat.proexpense_backend.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.controller.docs.CategoryControllerDocs;
import com.mat.proexpense_backend.dto.request.RegisterCategoryRequest;
import com.mat.proexpense_backend.dto.request.UpdateCategoryRequest;
import com.mat.proexpense_backend.dto.response.CategoryResponse;
import com.mat.proexpense_backend.service.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController implements CategoryControllerDocs {

  private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

  private final CategoryService service;

  public CategoryController(CategoryService service) {
    this.service = service;
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> findCategoryById(@PathVariable UUID id) {
    logger.info("finding category by id: {}", id);
    return ResponseEntity.ok(this.service.findCategoryById(id));
  }

  @Override
  @GetMapping("/accounts/{accountId}")
  public ResponseEntity<List<CategoryResponse>> findAllCategoriesByAccountId(@PathVariable UUID accountId) {
    logger.info("finding all categories by account id: {}", accountId);
    return ResponseEntity.ok(this.service.findAllCategoriesByAccountId(accountId));
  }

  @Override
  @GetMapping("/accounts/{accountId}/active")
  public ResponseEntity<List<CategoryResponse>> findActiveCategoriesByAccountId(@PathVariable UUID accountId) {
    logger.info("finding active categories by account id: {}", accountId);
    return ResponseEntity.ok(this.service.findActiveCategoriesByAccountId(accountId));
  }

  @Override
  @PostMapping
  public ResponseEntity<CategoryResponse> registerCategory(@Valid @RequestBody RegisterCategoryRequest request) {
    logger.info("registering category");
    CategoryResponse response = this.service.registerCategory(request);
    return ResponseEntity.created(URI.create("/api/v1/categories/" + response.id())).body(response);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> updateCategory(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryRequest request) {
    logger.info("updating category {}", id);
    return ResponseEntity.ok(this.service.updateCategory(id, request));
  }
}
