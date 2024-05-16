package com.example.thymeleaf.controllers;

import com.example.thymeleaf.Student;
import com.example.thymeleaf.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class HtmlController {

  private final StudentService studentService;

  @GetMapping
  public String index(Model model, HttpSession session) {
    if (session.getAttribute("students") == null) {
      session.setAttribute("students", new HashSet<Student>());
    }
    model.addAttribute("student", new Student());
    return "index";
  }

  @PostMapping
  public String addElement(@ModelAttribute Student student, HttpSession session) {
    Set<Student> studentSet = (Set<Student>) session.getAttribute("students");
    studentSet.add(student);
    session.setAttribute("students", studentSet);
    return "redirect:/";
  }
}
