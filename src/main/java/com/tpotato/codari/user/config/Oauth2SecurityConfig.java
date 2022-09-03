package com.tpotato.codari.user.config;

import com.tpotato.codari.user.dao.HttpCookieOAuth2AuthorizationRequestRepository;
import com.tpotato.codari.user.service.CodariOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class Oauth2SecurityConfig  {

  private final CodariOauth2UserService oAuth2UserService;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
    return http
          .cors()
        .and()

        .csrf()
            .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
        .and()

        .authorizeExchange()
              .pathMatchers(HttpMethod.OPTIONS).permitAll()
              .pathMatchers("/", "/error","/login/**", "/oauth2/**").permitAll()
              .anyExchange().authenticated()
        .and()

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

        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // stateless방식의 애플리케이션이 되도록 설정
        .oauth2Client(Customizer.withDefaults())
        .build();
//            .authenticationConverter(converter)
//            .authenticationManager(reactiveAuthenticationManager) // 인증 여부 체크
//            .authorizedClientRepository(authorizedClients)
//            .clientRegistrationRepository(clientRegistrations);
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
