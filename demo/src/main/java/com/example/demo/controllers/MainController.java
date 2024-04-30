package com.example.demo.controllers;

import com.example.demo.models.Family;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class MainController {

  @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

  @GetMapping("/test-redirect")
  public ResponseEntity<String> testRedirect() {
    return ResponseEntity.status(HttpStatus.FOUND)
      .header(HttpHeaders.LOCATION, "https://robert-programista.pl")
      .build();
  }

  @GetMapping("/getHeader")
  public void getHeader(HttpServletRequest request){
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      System.out.println(headerName + ": " + headerValue);
    }
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        System.out.println(cookie.getName() + ": " + cookie.getValue());
      }
    }
  }

  @GetMapping( "/getall")
  public List<Family> getAll(HttpServletResponse response){
    List<Family> familyList = new ArrayList<>();
    response.setHeader("Length", String.valueOf(familyList.size()));
    Cookie cookie = new Cookie("Length",String.valueOf(familyList.size()));
    cookie.setMaxAge(10);
    response.addCookie(cookie);
    return familyList;
  }

}
