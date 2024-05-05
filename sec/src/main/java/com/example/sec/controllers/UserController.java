package com.example.sec.controllers;

import com.example.sec.models.AuthRequest;
import com.example.sec.models.User;
import com.example.sec.repositories.UserRepository;
import com.example.sec.services.JwtService;
import com.example.sec.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserRepository entityRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserService createUser;

  @PostMapping("/new")
  public String addNewUser(@RequestBody User userInfo) {
    createUser.createUser(userInfo);
    return "User created";
  }

  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public List<User> getAllTheProducts() {
    return entityRepository.findAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public User getProductById(@PathVariable long id) {
    return entityRepository.findById(id).orElseThrow(RuntimeException::new);
  }

  @PostMapping("/authenticate")
  public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    try{
      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
      if (authentication.isAuthenticated()) {
        return jwtService.generateToken(authRequest.getUsername());
      } else {
        throw new UsernameNotFoundException("invalid user request !");
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

}
