package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import com.tpotato.codari.user.domain.enumerator.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_user_oauth")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class UserOauth extends CommonEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long userOauthId;

  @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  public User userId;

  @NotNull
  @Enumerated(EnumType.STRING)
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
