package com.mat.proexpense_backend.dto.response;

public record SignInResponse(
  String token,
  String type
) {

  public static SignInResponse bearer(String token) {
    return new SignInResponse(token, "Bearer");
  }
}
