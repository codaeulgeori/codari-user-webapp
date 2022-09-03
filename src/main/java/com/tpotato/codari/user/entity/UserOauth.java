package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.tpotato.codari.user.domain.enumerator.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class UserOauth extends CommonEntity{
  @Id
  public Long userOauthId;

  public Long userId;

  public AuthProvider providerName;
  public String providerId;
  public String userName;
  public String userProfileImage;
  public String userEmail;

  public UserOauth update(String name, String imageUrl) {
    this.userEmail = name;
    this.userProfileImage = imageUrl;
    return this;
  }
}
