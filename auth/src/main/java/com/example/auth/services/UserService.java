package com.example.auth.services;

import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.entity.UserRegisterDTO;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private User saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.saveAndFlush(user);
  }

  public String generateToken(String username) {
    return jwtService.generateToken(username);
  }

  public void validateToken(String token) {
    jwtService.validateToken(token);
  }


  public void register(UserRegisterDTO userRegister) {
    User user = new User();
    user.setLogin(userRegister.getLogin());
    user.setPassword(userRegister.getPassword());
    user.setEmail(userRegister.getEmail());
    if (userRegister.getRole() != null) {
      user.setRole(userRegister.getRole());
    } else {
      user.setRole(Role.USER);
    }
    saveUser(user);
  }
}
