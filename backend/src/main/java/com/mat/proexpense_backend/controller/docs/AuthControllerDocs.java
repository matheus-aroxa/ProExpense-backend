package com.mat.proexpense_backend.controller.docs;

import org.springframework.http.ResponseEntity;
import com.mat.proexpense_backend.dto.request.SignInRequest;
import com.mat.proexpense_backend.dto.request.SignUpRequest;
import com.mat.proexpense_backend.dto.response.SignInResponse;
import com.mat.proexpense_backend.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "User registration and sign in")
@SecurityRequirements
public interface AuthControllerDocs {

  @Operation(
      summary = "Sign in",
      description = "Authenticates the user and returns a JWT that must be sent in the Authorization header as `Bearer <token>`")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "401", description = "Invalid email or password")
  })
  ResponseEntity<SignInResponse> signIn(SignInRequest request);

  @Operation(summary = "Sign up", description = "Registers a new user")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User registered"),
      @ApiResponse(responseCode = "400", description = "Invalid request body"),
      @ApiResponse(responseCode = "409", description = "Email already in use")
  })
  ResponseEntity<UserResponse> signUp(SignUpRequest request);
}
