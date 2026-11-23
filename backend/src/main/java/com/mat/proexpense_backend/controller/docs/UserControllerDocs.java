package com.mat.proexpense_backend.controller.docs;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.mat.proexpense_backend.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "User lookup")
public interface UserControllerDocs {

  @Operation(summary = "Find user by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User found"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  ResponseEntity<UserResponse> findUserById(@Parameter(description = "User ID") UUID id);

  @Operation(summary = "List all users")
  @ApiResponse(responseCode = "200", description = "Users found")
  ResponseEntity<List<UserResponse>> findAllUsers();
}
