package com.tpotato.codari.user.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "app-security")
public class AppSecurityProperties {
  private final Auth auth = new Auth();
  private final OAuth2 oauth2 = new OAuth2();

  @Builder @Getter @Setter
  @AllArgsConstructor @NoArgsConstructor
  public static class Auth {
    private String tokenSecret;
    private long tokenExpirationMsec;
  }
  @Getter
  @AllArgsConstructor @NoArgsConstructor
  public static final class OAuth2 {
    private List<String> authorizedRedirectUris = new ArrayList<>();
  }
}
