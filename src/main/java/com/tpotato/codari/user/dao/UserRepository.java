package com.tpotato.codari.user.dao;

import com.tpotato.codari.user.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
  @Query("SELECT * FROM TB_USER U" +
      "LEFT JOIN TB_USER_OAUTH UO ON U.userId = UO.userId " +
      "WHERE U.email = :email")
  Mono<User> findByEmail(String email);
}
