package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

@Builder @Data @NoArgsConstructor @AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table("tb_user")
public class User extends CommonEntity{
  @Id
  public Long userId;

  public String codariName;
  public String profileImage;
  public String email;
  public String grade;
  public String locationExposure;
  public String additionalVerificationCode;
}
