package com.tpotato.codari.user.controller;

import com.tpotato.codari.user.domain.UserPrincipal;
import com.tpotato.codari.user.domain.dto.ResponseData;
import com.tpotato.codari.user.domain.dto.UserProfile;
import com.tpotato.codari.user.service.UserService;
import com.tpotato.codari.user.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = {"/user/v1", "/user"})
public class UserController {
  private final UserService userService;

  @Value("${codari.token.name}")
  String tokenName;

  @Value("${codari.token.cookie.ageSec}")
  String tokenCookieAgeSec;

  @RequestMapping(method = RequestMethod.GET)
  public Mono<ResponseData> getUser(ServerWebExchange exchange){
    Mono<ResponseData> responseData = CookieUtils.getCookie(exchange.getRequest(), "user_data")
        .map(httpCookie -> (UserPrincipal)CookieUtils.deserialize(httpCookie, UserPrincipal.class))
        .map(userPrincipal -> {
          return ResponseData.builder().resultData(userPrincipal).build();
        });
    return responseData;
  }

  @RequestMapping(method = RequestMethod.POST)
  public Mono<ResponseData> register(@RequestBody UserProfile userProfile,
                                     ServerWebExchange exchange) {
    Mono<Authentication> authentication = CookieUtils.getCookie(exchange.getRequest(), "user_data")
        .map(httpCookie -> CookieUtils.deserialize(httpCookie, Authentication.class));

    return authentication.flatMap((auth) ->
          userService.registerNewUserAndMakeJwt(auth, userProfile)
              .doOnSuccess(jwt -> {
                CookieUtils.addCookie(exchange.getResponse(), tokenName, jwt, Integer.parseInt(tokenCookieAgeSec));
              })
        )
        .map((res) -> ResponseData.builder()
            .resultData(res).build());
  }
}
