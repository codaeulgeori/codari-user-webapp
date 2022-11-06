package com.tpotato.codari.user.dao;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.tpotato.codari.user.util.CookieUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.tpotato.codari.user.util.CookieUtils.COOKIE_EXPIRE_SECONDS;

/**
 * 인증 요청을 coockie 에 저장하고 검색
 */
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";


    public void removeAuthorizationRequestCookies(ServerHttpRequest request, ServerHttpResponse response) {
//        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
//        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> loadAuthorizationRequest(ServerWebExchange exchange) {
        return CookieUtils.getCookie(exchange.getRequest(), OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
            .or(null);
    }

    @Override
    public Mono<Void> saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, ServerWebExchange exchange) {
        // redirect uri 는 http://localhost:8080/login/oauth2/code/kakao 가 들어가게 된다.
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(exchange.getRequest(), exchange.getResponse(), OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(exchange.getRequest(), exchange.getResponse(), REDIRECT_URI_PARAM_COOKIE_NAME);
            return Mono.empty();
        }

        CookieUtils.addCookie(exchange.getResponse(), OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), null, COOKIE_EXPIRE_SECONDS);

        // 요기는 http://localhost:3000/oauth2/redirect 가 들어간다.
        String redirectUriAfterLogin = exchange.getRequest().getQueryParams().getFirst(REDIRECT_URI_PARAM_COOKIE_NAME);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(exchange.getResponse(), REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, null, COOKIE_EXPIRE_SECONDS);
        }
        return Mono.empty();
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> removeAuthorizationRequest(ServerWebExchange exchange) {
        return this.loadAuthorizationRequest(exchange)
            .map(oAuth2AuthorizationRequest -> {
                removeAuthorizationRequestCookies(exchange.getRequest(), exchange.getResponse());
                return oAuth2AuthorizationRequest;
            });
    }
}
