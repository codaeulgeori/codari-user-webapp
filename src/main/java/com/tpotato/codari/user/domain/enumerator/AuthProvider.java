package com.tpotato.codari.user.domain.enumerator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthProvider {
    LOCAL("TEST"),
    KAKAO("K"),
    NAVER("N"),
    GOOGLE("G"),
    FACEBOOK("F"),
    GITHUB("GH");

    public String code;
}
