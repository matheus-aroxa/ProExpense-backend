package com.mat.proexpense_backend.controller.docs;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.mat.proexpense_backend.dto.request.RegisterCategoryRequest;
import com.mat.proexpense_backend.dto.request.UpdateCategoryRequest;
import com.mat.proexpense_backend.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Categories", description = "Expense categories of an account")
public interface CategoryControllerDocs {

  @Operation(summary = "Find category by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category found"),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })
  ResponseEntity<CategoryResponse> findCategoryById(@Parameter(description = "Category ID") UUID id);

  @Operation(summary = "List categories of an account", description = "Returns both active and inactive categories")
  @ApiResponse(responseCode = "200", description = "Categories found")
  ResponseEntity<List<CategoryResponse>> findAllCategoriesByAccountId(@Parameter(description = "Account ID") UUID accountId);

  @Operation(summary = "List active categories of an account")
  @ApiResponse(responseCode = "200", description = "Active categories found")
  ResponseEntity<List<CategoryResponse>> findActiveCategoriesByAccountId(@Parameter(description = "Account ID") UUID accountId);

  @Operation(summary = "Register category", description = "Category names are unique per account, ignoring case")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Category registered"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "404", description = "Account not found"),
      @ApiResponse(responseCode = "409", description = "A category with this name already exists in the account")
  })
  ResponseEntity<CategoryResponse> registerCategory(RegisterCategoryRequest request);

  @Operation(summary = "Update category", description = "Category names are unique per account, ignoring case")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category updated"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "404", description = "Category not found"),
      @ApiResponse(responseCode = "409", description = "A category with this name already exists in the account")
  })
  ResponseEntity<CategoryResponse> updateCategory(@Parameter(description = "Category ID") UUID id, UpdateCategoryRequest request);
}
