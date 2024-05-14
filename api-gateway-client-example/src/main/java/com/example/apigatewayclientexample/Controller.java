package com.example.apigatewayclientexample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @Value("${uid}")
  private String uid;

  @GetMapping("/getuid")
  public String getUUId() {
    return uid;
  }
}
