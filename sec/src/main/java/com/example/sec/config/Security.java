package com.example.sec.config;

import com.example.sec.filters.JwtFilter;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //zabezpieczenie metod pre authorize post auth itd.
@RequiredArgsConstructor
public class Security {
  private final UserRepository userRepository;
  private final JwtFilter jwtFilter;

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserEntityDetailsService(userRepository);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    /** @Deprecated
     *  http
     *             // użyj csrf(Customizer.withDefaults()) by korzystać z domyślnych ustawień
     *             .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
     *             /* reszta konfiguracji
     *
     czyli coś takiego:
    http
    .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) // zmień na tę linijkę
    .authorizeRequests(authorizeRequests ->
    authorizeRequests
    .antMatchers("/hello","/new","/authenticate").permitAll() // każdy może korzystać
    .anyRequest().authenticated() // każdy inny endpoint wymaga autoryzacji
    )
    .sessionManagement(sessionManagement ->
    sessionManagement
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    )
    .authenticationProvider(authenticationProvider())
    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
     *
     *             */
    http.csrf().disable()
      .authorizeRequests(authorizeRequests ->
        authorizeRequests
          .requestMatchers("/hello","/new","/authenticate").permitAll() //każdy może korzystać
          .anyRequest().authenticated() //każdy inny endpoint wymaga autoryzacji
      )
      .sessionManagement(sessionManagement ->
        sessionManagement
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authenticationProvider(authenticationProvider())
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
    /*
csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) - Włącza ochronę przed atakami CSRF (Cross-Site Request Forgery). Token CSRF jest generowany i przechowywany w plikach cookie. Flag withHttpOnlyFalse pozwala JavaScriptowi na odczytanie plików cookie, co jest niezbędne do przesyłania tokena CSRF wraz z żądaniem.
.antMatchers("/hello","/new","/authenticate").permitAll() - Te ścieżki są dostępne dla wszystkich, niezależnie od tego, czy użytkownik jest zalogowany, czy nie.
.anyRequest().authenticated() - Wszystkie inne żądania muszą być zalogowane. Innymi słowy, tylko uwierzytelnione żądania mogą uzyskać dostęp.
.sessionCreationPolicy(SessionCreationPolicy.STATELESS) - To mówi Springowi, że sesja nie będzie używana do przechowywania stanu użytkownika. Jest to typowe dla aplikacji RESTful i bezstanowych systemów uwierzytelniania, takich jak JWT (Json Web Token).
.authenticationProvider(authenticationProvider()) - Określa dostawcę uwierzytelniania, który odpowiada za uwierzytelnianie żądań.
.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); - Dodaje filtr JWT przed filtrem uwierzytelniania nazwa użytkownika/hasło. Filtr JWT jest stosowany do przetwarzania żądań, które wymagają uwierzytelniania.

    return http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
      authorizationManagerRequestMatcherRegistry.requestMatchers("/login").permitAll(); //dostęp dozwolony dla wszystkich
      authorizationManagerRequestMatcherRegistry.anyRequest().authenticated(); //dostęp zabroniony dla wszystkich
    }).build();*/
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
