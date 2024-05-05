package com.example.sec.services;

import com.example.sec.auth.UserEntityDetails;
import com.example.sec.models.User;
import com.example.sec.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
//serwis dostarcza szczegóły dotyczace biezacego "zalogowanego użytkownika" lub zwraca not fount w przypadku braku możliwości odnalezienia jegod danych w bazie
public class UserEntityDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByMail(username);
    return user.map(UserEntityDetails::new).orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
