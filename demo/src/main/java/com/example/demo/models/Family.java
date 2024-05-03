package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "family")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Family {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private String origin;

  /*@OneToMany(mappedBy = "family", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Member> members = new ArrayList<>();*/

  @OneToOne
  @JoinColumn(name = "head")
  private Member head;
}
