package com.example.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@AllArgsConstructor
public class DogApiService {
  RestTemplate restTemplate;

  public ResponseEntity<?> getRandomDogPicture() throws URISyntaxException {
    URI apiUrl = new URI("https://dog.ceo/api/breeds/image/random");
    HttpHeaders headers = new HttpHeaders();
    //headers.set("API-KEY", "KRY");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
    return ResponseEntity.ok(response.getBody());
  }
}
