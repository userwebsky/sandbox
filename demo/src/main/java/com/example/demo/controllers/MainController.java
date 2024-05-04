package com.example.demo.controllers;

import com.example.demo.models.FamilyDTO;
import com.example.demo.services.DogApiService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1")
public class MainController {
  private final DogApiService dogApiService;

  @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

  @GetMapping("/random-dog")
  public ResponseEntity<?> randomDog() throws URISyntaxException {
    return dogApiService.getRandomDogPicture();
  }

  @GetMapping("/test-redirect")
  public ResponseEntity<String> testRedirect() {
    return ResponseEntity.status(HttpStatus.FOUND)
      .header(HttpHeaders.LOCATION, "https://robert-programista.pl")
      .build();
  }

  @GetMapping("/getHeader")
  public void getHeader(HttpServletRequest request){
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      System.out.println(headerName + ": " + headerValue);
    }
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        System.out.println(cookie.getName() + ": " + cookie.getValue());
      }
    }
  }

  @GetMapping( "/getall")
  public List<FamilyDTO> getAll(HttpServletResponse response){
    List<FamilyDTO> familyDTOList = new ArrayList<>();
    response.setHeader("Length", String.valueOf(familyDTOList.size()));
    Cookie cookie = new Cookie("Length",String.valueOf(familyDTOList.size()));
    cookie.setMaxAge(10);
    response.addCookie(cookie);
    return familyDTOList;
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile() throws IOException {

    File file = new File("src/main/resources/static/test.txt");
    Resource resource = new FileSystemResource(file);

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
      .contentLength(file.length())
      .contentType(MediaType.parseMediaType("application/octet-stream"))
      .body(resource);
  }

  @GetMapping("/video")
  public StreamingResponseBody streamVideo(HttpServletResponse response) throws IOException {
    response.setContentType("video/mp4");
    //InputStream videoFileStream = new FileInputStream(new File("path/asynchronicznosc.mp4"));
    URL url = new URL("https://robert-programista.pl/wp-content/uploads/2022/02/asynchronicznosc.mp4");
    InputStream videoFileStream = url.openStream();
    return outputStream -> {
      int nRead;
      byte[] data = new byte[1024];
      while ((nRead = videoFileStream.read(data, 0, data.length)) != -1) {
        outputStream.write(data, 0, nRead);
      }
      videoFileStream.close();
    };
  }

}
