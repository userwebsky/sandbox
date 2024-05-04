package com.example.sec.auth;

import com.example.sec.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserEntityDetails implements UserDetails {
  private String mail;
  private String password;
  private boolean isEnabled;
  private List<GrantedAuthority> authorities;

  public UserEntityDetails(User user) {
    this.mail = user.getMail();
    this.password = user.getPassword();
    this.isEnabled = user.isLoginDisabled();
    this.authorities = Arrays.stream(user.getRoles().split(","))
      .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.mail;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return this.isEnabled;
  }
}
