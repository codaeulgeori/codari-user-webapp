package com.tpotato.codari.user.entity;

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
@Table(name = "tb_user_favorite_board")
public class UserFavoriteBoard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long userFacoriteBoardId;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  public User userId;

  public Long boardId;
  public String boardTye;
}
