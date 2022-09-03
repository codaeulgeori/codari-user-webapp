package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class CodariAddr extends CommonEntity{
  @Id
  public Long codariAddrId;


  public Long codariId;

  public String firstAddr;
  public String secondAddr;
  public String thirdAddr;
  public String detailAddr;

  public String latitude;
  public String longitude;
}
