package com.mat.proexpense_backend.entitties;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "users")
@Data
@RequiredArgsConstructor
public class User {

  @Id 
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @NotNull
  private String name;

  @Email
  @NotNull
  private String email;

  @NotNull
  private String password;
}
