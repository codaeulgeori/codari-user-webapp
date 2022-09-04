package com.tpotato.codari.user.domain.enumerator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthProvider {
    LOCAL("TEST"),
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github");

    public String code;
}
