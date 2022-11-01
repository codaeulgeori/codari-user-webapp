package com.tpotato.codari.user.dao;

import com.tpotato.codari.user.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
  @Query("SELECT * FROM tb_user " +
      "WHERE email = :email")
  Mono<User> findByEmail(String email);


  Mono<Void> deleteByUserId(Long userId);
}
