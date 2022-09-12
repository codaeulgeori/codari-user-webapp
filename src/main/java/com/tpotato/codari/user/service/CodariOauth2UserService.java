package com.tpotato.codari.user.service;

import com.tpotato.codari.user.dao.UserOauthRepository;
import com.tpotato.codari.user.dao.UserRepository;
import com.tpotato.codari.user.domain.UserPrincipal;
import com.tpotato.codari.user.domain.enumerator.AuthProvider;
import com.tpotato.codari.user.domain.oauth2.OAuth2UserInfo;
import com.tpotato.codari.user.domain.oauth2.OAuth2UserInfoFactory;
import com.tpotato.codari.user.entity.User;
import com.tpotato.codari.user.entity.UserOauth;
import com.tpotato.codari.user.exception.BuildDataFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Slf4j @RequiredArgsConstructor
@Service
public class CodariOauth2UserService extends DefaultReactiveOAuth2UserService {

  private final UserRepository userRepository;
  private final UserOauthRepository userOauthRepository;

  @Transactional
  @Override
  public Mono<OAuth2User> loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    Mono<OAuth2User> oAuth2User = super.loadUser(oAuth2UserRequest);

    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      log.error("loadUser() : ",ex);
      // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  /**
   * 사용자 정보 추출
   * @param oAuth2UserRequest
   * @param oAuth2User
   * @return
   */
  private Mono<OAuth2User> processOAuth2User(OAuth2UserRequest oAuth2UserRequest, Mono<OAuth2User> oAuth2User) {
    return oAuth2User.map((user) ->
            OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), user.getAttributes())
          )
        .filter((oAuth2UserInfo -> !StringUtils.isEmpty(oAuth2UserInfo.getEmail())))
        .flatMap(oAuth2UserInfo ->
          userRepository.findByEmail(oAuth2UserInfo.getEmail())
              .flatMap(user -> updateExistingUser(user, oAuth2UserInfo))
              .switchIfEmpty(makeUserWithOauth2UserInfo(oAuth2UserInfo))

              .map(user -> UserPrincipal.create(user, oAuth2UserInfo.getAttributes()))
        );
  }

  private Mono<User> makeUserWithOauth2UserInfo(OAuth2UserInfo userInfo) {
    User user = User.builder()
        .email(userInfo.getEmail())
        .build();
    return Mono.just(user);
  }


  /**
   * DB에 존재하지 않는 경우 새로 등록
   * @param oAuth2UserRequest
   * @param oAuth2UserInfo
   * @return
   */
  private Mono<User> registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user;
    UserOauth userOauth;
    try {
      userOauth =
          UserOauth.builder()
              .providerId(oAuth2UserInfo.getId())
              .vender(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))
              .userEmail(oAuth2UserInfo.getEmail())
              .userProfileImage(oAuth2UserInfo.getImageUrl())
              .userName(oAuth2UserInfo.getName())
              .build();
      user = User.builder()
          .codariName(oAuth2UserInfo.getName())
          .profileImage(oAuth2UserInfo.getImageUrl())
          .email(oAuth2UserInfo.getEmail())
          .additionalVerificationCode("0000")
          .locationExposure("N")
          .grade("?")
          .build();
    } catch (Exception e) {
      throw new BuildDataFailException("registerNewUser - oAuth2UserInfo : "+ oAuth2UserInfo, e);
    }
    return userRepository.save(user)
        .flatMap(savedUser -> {
          user.setUserId(savedUser.userId);
          user.setCreatedDatetime(savedUser.getCreatedDatetime());
          user.setUpdateDatetime(savedUser.getUpdateDatetime());

          userOauth.setUserId(savedUser.userId);
          return userOauthRepository.save(userOauth);
        })
        .flatMap(savedOauth -> Mono.just(user))
        ;
  }

  /**
   * DB에 존재할 경우 정보 업데이트
   * @param existingUser
   * @param oAuth2UserInfo
   * @return
   */
  private Mono<User> updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    userOauthRepository.save(UserOauth.builder()
        .userId(existingUser.userId)
        .userProfileImage(oAuth2UserInfo.getImageUrl())
        .userName(oAuth2UserInfo.getName())
        .build());

    return Mono.just(existingUser);
  }

}
