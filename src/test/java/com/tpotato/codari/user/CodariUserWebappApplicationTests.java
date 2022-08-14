package com.tpotato.codari.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class CodariUserWebappApplicationTests {

  @Test
  void contextLoads() {
  }

  @Bean
  public FlywayMigrationStrategy cleanMigrateStrategy() {
    return flyway -> {
      flyway.repair();
      flyway.migrate();
    };
  }
}
