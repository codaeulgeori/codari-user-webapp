package com.tpotato.codari.user.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SerializationUtils;
import reactor.core.publisher.Mono;
import java.util.Base64;

@Slf4j
public class CookieUtils {
  public static final int COOKIE_EXPIRE_SECONDS = 180;
  public static final String CODARI_DOMAIN = "cooodari.com";

  public static Mono<HttpCookie> getCookie(ServerHttpRequest request, String name) {
    MultiValueMap<String, HttpCookie> cookies = request.getCookies();
    return Mono.just(cookies.getFirst(name));
  }

  public static void addCookie(ServerHttpResponse response, String name, String value, int maxAge) {
    response.addCookie(ResponseCookie.from(name, value)
            .domain(CODARI_DOMAIN)
            .path("/")
            .httpOnly(true)
            .maxAge(maxAge)
        .build());
  }

  public static void deleteCookie(ServerHttpRequest request, ServerHttpResponse response, String name) {
    MultiValueMap<String, HttpCookie> cookies = request.getCookies();
    cookies.forEach((k,httpCookies) -> {
      if (k.equals(name)) {
        for (HttpCookie cookie: httpCookies) {
          response.addCookie(ResponseCookie.from(cookie.getName(), cookie.getValue()).build());
          log.info("cookie deleted : {}", cookie);
        }
      }
    });
  }

  public static String serialize(Object object) {
    return Base64.getUrlEncoder()
        .encodeToString(SerializationUtils.serialize(object));
  }

  public static <T> T deserialize(HttpCookie cookie, Class<T> cls) {
    return cls.cast(SerializationUtils.deserialize(
        Base64.getUrlDecoder().decode(cookie.getValue())));
  }


}
