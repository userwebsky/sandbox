package com.example.sec.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(generator="user_id_seq", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name="user_id_seq", sequenceName="user_id_seq", allocationSize = 1)
  private Long id;

  @Column(unique = true)
  private String mail;
  private String password;
  @Column(name = "logindisabled")
  private boolean loginDisabled;
  private String roles;
}
