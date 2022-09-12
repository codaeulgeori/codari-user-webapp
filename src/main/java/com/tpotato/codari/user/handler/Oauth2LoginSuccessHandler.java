package com.tpotato.codari.user.handler;


import com.tpotato.codari.user.domain.UserPrincipal;
import com.tpotato.codari.user.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.tpotato.codari.user.dao.HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static com.tpotato.codari.user.util.CookieUtils.COOKIE_EXPIRE_SECONDS;


@Slf4j
@Component
public class Oauth2LoginSuccessHandler implements ServerAuthenticationSuccessHandler {

  //TODO : 회원가입시 작성하는 프로필 작성 페이지로 redirect
  private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
  private ServerRequestCache requestCache = new WebSessionServerRequestCache();

  public Oauth2LoginSuccessHandler() {
  }

  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    ServerWebExchange exchange = webFilterExchange.getExchange();

    if (isUserNewbie(authentication)) {
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      userPrincipal.setCode(exchange.getRequest().getQueryParams().getFirst("code"));
      userPrincipal.setState(exchange.getRequest().getQueryParams().getFirst("state"));
      CookieUtils.addCookie(exchange.getResponse(), "user_data", CookieUtils.serialize(authentication.getPrincipal()), COOKIE_EXPIRE_SECONDS);

      return this.requestCache.getRedirectUri(exchange)
          .defaultIfEmpty(URI.create("/user/v1"))
          .flatMap((location) -> {
            return this.redirectStrategy.sendRedirect(exchange, location);
          });
    } else {
      //TODO : 기존에 있는 유저면 jwt token 생성 후 homepage 로 redirect
      return this.requestCache.getRedirectUri(exchange)
          .defaultIfEmpty(URI.create("/"))
          .flatMap((location) -> {
            return this.redirectStrategy.sendRedirect(exchange, location);
          });
    }

  }

  private boolean isUserNewbie(Authentication authentication) {
    return ((UserPrincipal)authentication.getPrincipal()).getId() == null;
  }

  private Mono<OAuth2AuthorizationRequest> getOauth2CookieAndDeserialize(ServerWebExchange exchange) {
    return CookieUtils.getCookie(exchange.getRequest(), OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class));
  }

  public void setRequestCache(ServerRequestCache requestCache) {
    Assert.notNull(requestCache, "requestCache cannot be null");
    this.requestCache = requestCache;
  }

  public void setRedirectStrategy(ServerRedirectStrategy redirectStrategy) {
    Assert.notNull(redirectStrategy, "redirectStrategy cannot be null");
    this.redirectStrategy = redirectStrategy;
  }
}
