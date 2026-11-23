package com.mat.proexpense_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.controller.docs.PingControllerDocs;

@RestController
@RequestMapping("api/v1/ping")
public class PingController implements PingControllerDocs {

  @Override
  @GetMapping
  public ResponseEntity<Void> ping() {
    return ResponseEntity.noContent().build();
  }
}
