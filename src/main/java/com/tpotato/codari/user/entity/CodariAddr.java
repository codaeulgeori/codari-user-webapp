package com.tpotato.codari.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "tb_codari_addr")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class CodariAddr extends CommonEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long codariAddrId;

  @OneToOne(targetEntity = Codari.class, fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "codari_id", nullable = false)
  public Codari codariId;

  public String firstAddr;
  public String secondAddr;
  public String thirdAddr;
  public String detailAddr;

  public String latitude;
  public String longitude;
}
