package com.example.demo.services;

import com.example.demo.student.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
  List<Student> students;

  public StudentService() {
    students = getStudentsMock();
  }

  public Student getStudentById(long id) {
    return students.stream().filter(student -> student.getId() == id).findFirst().orElse(null);
  }

  private List<Student> getStudentsMock() {
    List<Student> students = new ArrayList<>();
      Student student1 = new Student();
      student1.setId(1);
      student1.setName("John");
      student1.setSurname("Doe");
      student1.setGrade("A");
      students.add(student1);

      Student student2 = new Student();
      student2.setId(2);
      student2.setName("Jane");
      student2.setSurname("Smith");
      student2.setGrade("B");
      students.add(student2);

      return students;
  }
}
