package com.tpotato.codari.user.dao;

import com.tpotato.codari.user.domain.enumerator.AuthProvider;
import com.tpotato.codari.user.entity.User;
import com.tpotato.codari.user.entity.UserOauth;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserOauthRepository extends ReactiveCrudRepository<UserOauth, Long> {
  Mono<UserOauth> save(UserOauth userOauth);
  Mono<UserOauth> findByVenderAndProviderId(AuthProvider provider, Long providerId);
  Mono<Void> deleteByUserOauthId(Long userOauthId);
}
