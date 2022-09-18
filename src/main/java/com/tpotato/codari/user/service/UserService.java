package com.tpotato.codari.user.service;

import com.tpotato.codari.user.dao.UserOauthRepository;
import com.tpotato.codari.user.dao.UserRepository;
import com.tpotato.codari.user.domain.UserPrincipal;
import com.tpotato.codari.user.domain.dto.UserProfile;
import com.tpotato.codari.user.domain.enumerator.AuthProvider;
import com.tpotato.codari.user.domain.oauth2.OAuth2UserInfo;
import com.tpotato.codari.user.domain.oauth2.OAuth2UserInfoFactory;
import com.tpotato.codari.user.entity.User;
import com.tpotato.codari.user.entity.UserOauth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final UserOauthRepository userOauthRepository;

  @Transactional
  public Mono<User> registerNewUser(UserPrincipal userPrincipal, UserProfile userProfile) {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(userPrincipal.getVender(), userPrincipal.getAttributes());
    log.info("registerNewUser.oAuth2UserInfo : {}", oAuth2UserInfo);

    return userRepository.save(User.builder()
                  .email(oAuth2UserInfo.getEmail())
                  .codariName(userProfile.codariName)
                  .locationExposure("N")
                  .grade("?")
                  .additionalVerificationCode("0000")
                  .profileImage(oAuth2UserInfo.getImageUrl())
              .build())
        .flatMap(user ->
          userOauthRepository.save(UserOauth.builder()
              .userId(user.userId)
              .vender(AuthProvider.valueOf(userPrincipal.getVender().toUpperCase()))
              .providerId(oAuth2UserInfo.getId())
              .userName(oAuth2UserInfo.getName())
              .userEmail(oAuth2UserInfo.getEmail())
              .userProfileImage(oAuth2UserInfo.getImageUrl())
              .build())
              .map(userOauth -> user)
        );
  }
}
