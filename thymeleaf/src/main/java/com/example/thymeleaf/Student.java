package com.example.thymeleaf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
  private String uuid;
  private String name;
  private String surname;
  private String grade;
}
