package com.mat.proexpense_backend.controller.docs;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.mat.proexpense_backend.dto.request.RegisterCardRequest;
import com.mat.proexpense_backend.dto.request.UpdateCardRequest;
import com.mat.proexpense_backend.dto.response.CardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cards", description = "Payment cards of an account")
public interface CardControllerDocs {

  @Operation(summary = "Find card by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Card found"),
      @ApiResponse(responseCode = "404", description = "Card not found")
  })
  ResponseEntity<CardResponse> findCardById(@Parameter(description = "Card ID") UUID id);

  @Operation(summary = "List cards of an account", description = "Returns both active and inactive cards")
  @ApiResponse(responseCode = "200", description = "Cards found")
  ResponseEntity<List<CardResponse>> findAllCardsByAccountId(@Parameter(description = "Account ID") UUID accountId);

  @Operation(summary = "List active cards of an account")
  @ApiResponse(responseCode = "200", description = "Active cards found")
  ResponseEntity<List<CardResponse>> findActiveCardsByAccountId(@Parameter(description = "Account ID") UUID accountId);

  @Operation(summary = "Register card", description = "The owner must be an active member of the account")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Card registered"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(
          responseCode = "403",
          description = "Missing or invalid authentication token, or owner does not have access to the account")
  })
  ResponseEntity<CardResponse> registerCard(RegisterCardRequest request);

  @Operation(
      summary = "Update card",
      description = "The payment method cannot be changed to one that no longer supports the card's existing expenses")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Card updated"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "404", description = "Card not found"),
      @ApiResponse(responseCode = "409", description = "Card has expenses with a payment method that would no longer be supported")
  })
  ResponseEntity<CardResponse> updateCard(@Parameter(description = "Card ID") UUID id, UpdateCardRequest request);
}
