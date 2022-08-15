package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Builder @Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "tb_user")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class User extends CommonEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long userId;

  public String name;
  public String profileImage;
  public String email;
  public Character grade;
  public Character locationExposure;
  public String additionalVerificationCode;

  @OneToOne(mappedBy = "userId", fetch = FetchType.LAZY)
  public UserOauth oauthInfo;

}
