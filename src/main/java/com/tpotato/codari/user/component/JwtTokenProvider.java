package com.tpotato.codari.user.component;

import com.tpotato.codari.user.entity.User;
import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Authorization <-> JwtToken 변환
 */

@Slf4j @NoArgsConstructor
@Component
public class JwtTokenProvider {

  private static final String AUTHORITIES_KEY = "permissions";

  @Value("${app-security.auth.tokenSecret}")
  private String secret;

  @Value("${app-security.auth.tokenExpirationMsec}")
  private String expirationTime;
  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    var secret = Base64.getEncoder().encodeToString(this.secret.getBytes());
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String createToken(Authentication authentication) {
    String username = authentication.getName();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Claims claims = Jwts.claims().setSubject(username);
    if (authorities != null) {
      claims.put(AUTHORITIES_KEY
          , authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
    }

    Long expirationTimeLong = Long.parseLong(expirationTime);
    final Date createdDate = new Date();
    final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(createdDate)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }


  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();

    Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

    Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
        : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

    User principal = User.builder()
                          .email(claims.getSubject())
                          .build();
    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts
          .parser().setSigningKey(this.secretKey)
          .parseClaimsJws(token);
      //  parseClaimsJws will check expiration date. No need do here.
      log.info("expiration date: {}", claims.getBody().getExpiration());
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.info("Invalid JWT token: {}", e.getMessage());
      log.trace("Invalid JWT token trace.", e);
    }
    return false;
  }
}
