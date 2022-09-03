package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class UserAddr {
  @Id
  public Long userAddrId;

  public Long userId;

  public String firstAddr;
  public String secondAddr;
  public String thirdAddr;
  public String detailAddr;

  public String latitude;
  public String longitude;
}
