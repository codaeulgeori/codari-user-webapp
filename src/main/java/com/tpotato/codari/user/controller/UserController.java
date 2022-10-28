package com.tpotato.codari.user.controller;

import com.tpotato.codari.user.domain.UserPrincipal;
import com.tpotato.codari.user.domain.dto.ResponseData;
import com.tpotato.codari.user.domain.dto.UserProfile;
import com.tpotato.codari.user.domain.dto.UserWithdrawal;
import com.tpotato.codari.user.domain.enumerator.AuthProvider;
import com.tpotato.codari.user.service.UserService;
import com.tpotato.codari.user.util.CookieUtils;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = {"/user/v1", "/user"}, produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
  private final UserService userService;

  @Value("${codari.url}")
  String codariUrl;

  @Value("${codari.token.name}")
  String tokenName;

  @Value("${codari.token.cookie.ageSec}")
  String tokenCookieAgeSec;

  @RequestMapping(method = RequestMethod.GET)
  public Mono<ResponseData> getUser(ServerWebExchange exchange){
    Mono<ResponseData> responseData = CookieUtils.getCookie(exchange.getRequest(), "user_data")
        .map(httpCookie -> (Authentication)CookieUtils.deserialize(httpCookie, Authentication.class))
        .map(authentication -> {
          return ResponseData.builder().resultData(authentication).build();
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
                CookieUtils.addCookie(exchange.getResponse(), tokenName, jwt, codariUrl, Integer.parseInt(tokenCookieAgeSec));
              })
        )
        .map((res) -> ResponseData.builder()
            .resultData(res).build());
  }

  @RequestMapping(path = "/withdrawal/{provider}", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
  public Mono<ResponseData> withdrawal(@PathVariable("provider") AuthProvider provider,
                                       @RequestBody UserWithdrawal withdrawal) {
    log.info("withdrawal callback start provider : {}, UserWithdrawal : {}", provider, withdrawal);
    return userService.withdrawal(provider, withdrawal.user_id)
        .map(s -> ResponseData.builder()
                    .resultData(String.format("withdrawal complete - provider : %s, userId : %s", provider, withdrawal.user_id))
                    .build());
  }


  /**
   * FOR Test
   * @param exchange
   * @return
   */
  @RequestMapping(path = "/decodeJwt", method = RequestMethod.GET)
  public Mono<ResponseData> getUserWithJWT(ServerWebExchange exchange){
    Mono<ResponseData> responseData = CookieUtils.getCookie(exchange.getRequest(),tokenName)
        .filter(httpCookie -> tokenName.equals(httpCookie.getName()))
        .map(httpCookie -> userService.deserializeJWT(httpCookie.getValue()))
        .map(authentication -> {
          return ResponseData.builder().resultData(authentication).build();
        });
    return responseData;
  }
}
