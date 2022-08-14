package com.tpotato.codari.user.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class CommonEntity {
  @CreationTimestamp
  private LocalDateTime createdDatetime;

  @UpdateTimestamp
  private LocalDateTime updateDatetime;
}
