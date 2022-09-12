package com.tpotato.codari.user.domain;

import com.tpotato.codari.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class UserPrincipal implements OAuth2User, UserDetails, Serializable {
  private Long id;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;
  private String code;
  private String state;

  public UserPrincipal(Long id, String email, Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.authorities = authorities;
  }

  public static UserPrincipal create(User user) {
    List<GrantedAuthority> authorities = Collections.
        singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    return new UserPrincipal(
        user.getUserId(),
        user.getEmail(),
        authorities
    );
  }

  public static UserPrincipal create(User user, Map<String, Object> attributes) {
    UserPrincipal userPrincipal = UserPrincipal.create(user);
    userPrincipal.setAttributes(attributes);
    return userPrincipal;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return email;
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
    return false;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public Map<String, Object> getAttribute(String name) {
    return attributes;
  }
}
