package com.tpotato.codari.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "tb_codari")
public class Codari extends CommonEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long codariId;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  public User userId;

  public String codariName;

}
