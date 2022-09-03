package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.sql.Date;

@Builder @Data @NoArgsConstructor @AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class User extends CommonEntity{
  @Id
  public Long userId;

  public String name;
  public String profileImage;
  public String email;
  public Character grade;
  public Character locationExposure;
  public String additionalVerificationCode;
  public UserOauth oauthInfo;
}
