package com.tpotato.codari.user.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommonEntity {
  private LocalDateTime createdDatetime;
  private LocalDateTime updateDatetime;
}
