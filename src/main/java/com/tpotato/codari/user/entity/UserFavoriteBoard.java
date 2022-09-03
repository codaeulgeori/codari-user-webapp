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
public class UserFavoriteBoard extends CommonEntity{
  @Id
  public Long userFacoriteBoardId;

  public Long userId;

  public Long boardId;
  public String boardTye;
}
