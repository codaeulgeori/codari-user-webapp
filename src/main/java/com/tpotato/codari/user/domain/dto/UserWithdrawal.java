package com.tpotato.codari.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserWithdrawal {
  public Long user_id;
  public String referrer_type;
}
