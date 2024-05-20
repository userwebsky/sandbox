package com.example.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
  private final String timestamp;
  private final String message;
  private final Code code;

  public AuthResponse(Code code) {
    this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
    this.message = code.label;
    this.code = code;
  }
}
