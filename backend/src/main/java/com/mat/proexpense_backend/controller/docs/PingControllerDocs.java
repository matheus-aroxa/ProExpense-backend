package com.mat.proexpense_backend.controller.docs;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Health", description = "Service availability")
@SecurityRequirements
public interface PingControllerDocs {

  @Operation(
      summary = "Ping",
      description = "Lightweight availability check that does not touch the database. Used by keep-alive monitors")
  @ApiResponse(responseCode = "204", description = "Service is up")
  ResponseEntity<Void> ping();
}
