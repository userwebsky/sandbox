package com.example.thymeleaf.controllers;

import com.example.thymeleaf.Student;
import com.example.thymeleaf.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final StudentService studentService;

  @DeleteMapping
  public ResponseEntity<?> remove(@RequestParam String uuid, HttpSession session) {
    Set<Student> studentSet = (Set<Student>) session.getAttribute("students");
    studentSet.removeIf(value -> value.getUuid().equals(uuid));
    session.setAttribute("students", studentSet);
    return ResponseEntity.ok().build();
  }
}
