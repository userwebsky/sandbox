package com.example.sec.config;

import com.example.sec.repositories.UserRepository;
import com.example.sec.services.UserEntityDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //zabezpieczenie metod pre authorize post auth itd.
@RequiredArgsConstructor
public class Security {
  private final UserRepository userRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserEntityDetailsService(userRepository);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
      authorizationManagerRequestMatcherRegistry.requestMatchers("/login").permitAll(); //dostęp dozwolony dla wszystkich
      authorizationManagerRequestMatcherRegistry.anyRequest().authenticated(); //dostęp zabroniony dla wszystkich
    }).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {//kodowanie hasła
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) //zarządzanie autenyfikacją
    throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
