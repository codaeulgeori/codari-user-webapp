package com.tpotato.codari.user.service;

import com.tpotato.codari.user.dao.UserRepository;
import com.tpotato.codari.user.domain.UserPrincipal;
import com.tpotato.codari.user.domain.enumerator.AuthProvider;
import com.tpotato.codari.user.domain.oauth2.OAuth2UserInfo;
import com.tpotato.codari.user.domain.oauth2.OAuth2UserInfoFactory;
import com.tpotato.codari.user.entity.User;
import com.tpotato.codari.user.entity.UserOauth;
import com.tpotato.codari.user.exception.BuildDataFailException;
import com.tpotato.codari.user.exception.OAuth2AuthenticationProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j @RequiredArgsConstructor
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

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
  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
    if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }

    Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
    User user;
    if(userOptional.isPresent()) {
      user = userOptional.get();
      if(!user.oauthInfo.providerName.equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
        throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
            user.oauthInfo.providerName + " account. Please use your " + user.oauthInfo.providerName +
            " account to login.");
      }
      user = updateExistingUser(user, oAuth2UserInfo);
    } else {
      user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
    }

    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  /**
   * DB에 존재하지 않는 경우 새로 등록
   * @param oAuth2UserRequest
   * @param oAuth2UserInfo
   * @return
   */
  //TODO : oauthInfo table 에 userId 의 정보가 잘 들어가는지 확인 필요.
  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user;
    try {
      user = User.builder()
          .name(oAuth2UserInfo.getName())
          .email(oAuth2UserInfo.getEmail())
          .profileImage(oAuth2UserInfo.getImageUrl())
          .oauthInfo(
              UserOauth.builder()
                  .providerId(oAuth2UserInfo.getId())
                  .providerName(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                  .userEmail(oAuth2UserInfo.getEmail())
                  .userProfileImage(oAuth2UserInfo.getImageUrl())
                  .userName(oAuth2UserInfo.getName())
                  .build())
          .grade('?')
          .build();
    } catch (Exception e) {
      throw new BuildDataFailException("registerNewUser() oAuth2UserInfo : "+ oAuth2UserInfo);
    }
    return userRepository.save(user);
  }

  /**
   * DB에 존재할 경우 정보 업데이트
   * @param existingUser
   * @param oAuth2UserInfo
   * @return
   */
  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.oauthInfo.update(
        oAuth2UserInfo.getName(),
        oAuth2UserInfo.getImageUrl()
    );
    return userRepository.save(existingUser);
  }

}
