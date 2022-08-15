package com.tpotato.codari.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_user_phone")
public class UserPhone {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long userPhoneId;

  @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  public User userId;

  public String mobileOperator;
  public String phoneNumber;
}
