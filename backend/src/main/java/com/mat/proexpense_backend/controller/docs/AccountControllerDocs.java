package com.mat.proexpense_backend.controller.docs;

import java.util.List;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import com.mat.proexpense_backend.dto.request.AccountAccessRequest;
import com.mat.proexpense_backend.dto.request.AddUserToAccountRequest;
import com.mat.proexpense_backend.dto.request.RegisterAccountRequest;
import com.mat.proexpense_backend.dto.response.AccountAccessResponse;
import com.mat.proexpense_backend.dto.response.AccountMemberResponse;
import com.mat.proexpense_backend.dto.response.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Accounts", description = "Shared accounts and their members")
public interface AccountControllerDocs {

  @Operation(summary = "Register account", description = "Creates an account and adds the owner as its first member")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Account registered"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "404", description = "Owner not found")
  })
  ResponseEntity<AccountResponse> registerAccount(RegisterAccountRequest request);

  @Operation(summary = "Add user to account")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User added to the account"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "404", description = "Account or user not found"),
      @ApiResponse(responseCode = "409", description = "User is already a member of the account")
  })
  ResponseEntity<AccountResponse> addUserToAccount(AddUserToAccountRequest request);

  @Operation(summary = "List active members of an account")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Active members found"),
      @ApiResponse(responseCode = "404", description = "Account not found")
  })
  ResponseEntity<List<AccountMemberResponse>> findActiveMembers(@Parameter(description = "Account ID") UUID accountId);

  @Operation(summary = "Activate account member")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Member activated"),
      @ApiResponse(responseCode = "404", description = "Account not found or user is not a member of the account")
  })
  ResponseEntity<AccountResponse> activateMember(
      @Parameter(description = "Account ID") UUID accountId,
      @Parameter(description = "Member user ID") UUID userId);

  @Operation(summary = "Deactivate account member", description = "The account owner cannot be deactivated")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Member deactivated"),
      @ApiResponse(responseCode = "404", description = "Account not found or user is not a member of the account"),
      @ApiResponse(responseCode = "409", description = "Account owner cannot be deactivated")
  })
  ResponseEntity<AccountResponse> deactivateMember(
      @Parameter(description = "Account ID") UUID accountId,
      @Parameter(description = "Member user ID") UUID userId);

  @Operation(
      summary = "Check account access",
      description = "Tells whether the user is an active member of the account",
      parameters = {
          @Parameter(name = "userId", in = ParameterIn.QUERY, description = "User ID"),
          @Parameter(name = "accountId", in = ParameterIn.QUERY, description = "Account ID")
      })
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Access checked"),
      @ApiResponse(responseCode = "400", description = "Missing or invalid query parameters")
  })
  ResponseEntity<AccountAccessResponse> hasAccessToTheAccount(@ParameterObject AccountAccessRequest request);

  @Operation(summary = "List accounts of a user", description = "Returns every account the user is a member of")
  @ApiResponse(responseCode = "200", description = "Accounts found")
  ResponseEntity<List<AccountResponse>> findAllAccountsByUserId(@Parameter(description = "User ID") UUID userId);
}
