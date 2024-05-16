package com.example.thymeleaf;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class StudentService {

  private Set<Student> studentSet = new HashSet<>();

  public StudentService() {
    Student student = new Student();
    student.setName("Adam");
    student.setSurname("Mickiewicz");
    student.setGrade("A");
    student.setUuid(UUID.randomUUID().toString());

    Student student1 = new Student();
    student1.setName("Marek");
    student1.setSurname("Wójtowicz");
    student1.setGrade("C");
    student1.setUuid(UUID.randomUUID().toString());
    studentSet.add(student);
    studentSet.add(student1);
  }

  public Set<Student> getStudentSet() {
    return studentSet;
  }

  public void addStudent(Student student) {
    student.setUuid(UUID.randomUUID().toString());
    studentSet.add(student);
  }

  public void remove(String uuid) {
    studentSet.removeIf(value -> value.getUuid().equals(uuid));
  }
}
