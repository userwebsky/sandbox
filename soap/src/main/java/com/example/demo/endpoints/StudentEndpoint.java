package com.example.demo.endpoints;

import com.example.demo.services.StudentService;
import com.example.demo.student.GetStudent;
import com.example.demo.student.GetStudentResponse;
import com.example.demo.student.Student;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class StudentEndpoint {
  private final StudentService studentService;

  public StudentEndpoint(StudentService studentService) {
    this.studentService = studentService;
  }

  @PayloadRoot(namespace = "https://robert-programista.pl/soup-example", localPart = "getStudent")
  @ResponsePayload
  public GetStudentResponse getStudent(@RequestPayload GetStudent getStudent) {
    Student student = studentService.getStudentById(getStudent.getId());
    GetStudentResponse response = new GetStudentResponse();
    response.setStudent(student);
    return response;
  }
}
