package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_user_oauth")
@Data @NoArgsConstructor @AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class UserOauth extends CommonEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long userOauthId;

  public Long userId;

  public String vender;
  public String providerId;
  public String userName;
  public String userProfileImage;
  public String userEmail;
}
