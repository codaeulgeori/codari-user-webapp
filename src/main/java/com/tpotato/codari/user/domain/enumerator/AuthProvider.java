package com.tpotato.codari.user.domain.enumerator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthProvider {
    LOCAL("TEST"),
    KAKAO("kakao"),
    kakao("kakao"),
    NAVER("naver"),
    naver("naver"),
    GOOGLE("google"),
    google("google"),
    FACEBOOK("facebook"),
    GITHUB("github");

    public String code;
}
