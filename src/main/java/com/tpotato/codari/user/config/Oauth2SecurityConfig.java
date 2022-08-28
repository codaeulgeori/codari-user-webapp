package com.tpotato.codari.user.config;

import com.tpotato.codari.user.dao.HttpCookieOAuth2AuthorizationRequestRepository;
import com.tpotato.codari.user.service.CodariOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class Oauth2SecurityConfig  {

  private final CodariOauth2UserService oAuth2UserService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
          .cors()
        .and()
          .csrf(c -> c
              .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
          )
          .authorizeRequests()
            .antMatchers("/", "/error").permitAll()
            .anyRequest().authenticated()
        .and()
          .exceptionHandling(e -> e
              .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
          )
          .logout(l -> l
              .logoutSuccessUrl("/").permitAll()
          )
          .oauth2Login()
            .authorizationEndpoint()
              //client 처음 로그인 시도 uri
              .baseUri("/oauth2/authorize")
              .authorizationRequestRepository(cookieAuthorizationRequestRepository())
              .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService);
//        .failureHandler((request, response, exception) -> {
//          request.getSession().setAttribute("error.message", exception.getMessage());
//          handler.onAuthenticationFailure(request, response, exception);
//        });
    return http.build();
  }

  /**
   * 서비스를 stateless 로 두고 있기 떄문에,
   * JWT 를 사용하기 떄문에 session 에 저장할 필요가 없어져, Auth Request 를 Based64 encoded cookie로 저장
   * @return
   */
  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }
}
