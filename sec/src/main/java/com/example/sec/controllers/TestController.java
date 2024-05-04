package com.example.sec.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @GetMapping("/login")
  public String login() {
    return "Login successful";
  }

  @GetMapping("/logout")
  public String logout() {
        return "Logout successful";
  }
}
