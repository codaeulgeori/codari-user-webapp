package com.tpotato.codari.user.dao;

import com.tpotato.codari.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

}
