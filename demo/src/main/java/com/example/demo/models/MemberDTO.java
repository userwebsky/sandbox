package com.example.demo.models;

import com.example.demo.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class MemberDTO {
  private String name;
  private int age;
  private Gender gender;
}
