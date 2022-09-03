package com.tpotato.codari.user.config;

import com.tpotato.codari.user.dao.HttpCookieOAuth2AuthorizationRequestRepository;
import com.tpotato.codari.user.service.CodariOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class Oauth2SecurityConfig  {

  private final CodariOauth2UserService oAuth2UserService;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
    http
          .cors()
        .and()
          .csrf(c -> c
              .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
          )
        .authorizeExchange((exchange) -> {
              exchange.pathMatchers(HttpMethod.OPTIONS).permitAll()
              .pathMatchers("/", "/error","/login").permitAll()
              .anyExchange().authenticated();
        })
        .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
            .authenticationEntryPoint((exchange, ex) -> {
              return Mono.fromRunnable(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
              });
            })
            .accessDeniedHandler((exchange, denied) -> {
              return Mono.fromRunnable(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
              });
            }))
//          .logout(l -> l
//              .logoutSuccessUrl("/").permitAll()
//          )
//          .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
        .oauth2Login();
//
//
//          .oauth2Login()
//              .authenticationConverter(converter)
//              .authenticationManager(manager)
//              .authorizedClientRepository(authorizedClients)
//              .clientRegistrationRepository(clientRegistrations);
//              //client 처음 로그인 시도 uri
//              .baseUri("/oauth2/authorize")
//              .authorizationRequestRepository(cookieAuthorizationRequestRepository())
//              .and()
//                .userInfoEndpoint()
//                .userService(oAuth2UserService);
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
