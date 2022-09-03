package com.tpotato.codari.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPhone {
  @Id
  public Long userPhoneId;

  public Long userId;

  public String mobileOperator;
  public String phoneNumber;
}
