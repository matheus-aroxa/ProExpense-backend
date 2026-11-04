package com.mat.proexpense_backend.exception;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.groupingBy(
            FieldError::getField,
            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

    ProblemDetail body = ex.getBody();
    body.setDetail("Um ou mais campos são inválidos");
    body.setProperty("errors", errors);
    return handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail body = ProblemDetail.forStatusAndDetail(status, "Corpo da requisição inválido");

    String field = invalidField(ex);
    if (!field.isEmpty()) {
      body.setDetail("Um ou mais campos são inválidos");
      body.setProperty("errors", Map.of(field, List.of("Valor inválido")));
    }

    return handleExceptionInternal(ex, body, headers, status, request);
  }

  private String invalidField(HttpMessageNotReadableException ex) {
    if (!(ex.getCause() instanceof DatabindException cause)) {
      return "";
    }

    return cause.getPath().stream()
        .map(JacksonException.Reference::getPropertyName)
        .filter(Objects::nonNull)
        .collect(Collectors.joining("."));
  }
}
