package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Data //@Getter, @Setter
@ToString
@AllArgsConstructor
public class FamilyDTO {

  private final long id;
  @NonNull
  private String name;
  private String origin;
  private MemberDTO memberDTO;
}
