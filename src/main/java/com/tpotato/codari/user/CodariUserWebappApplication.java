package com.tpotato.codari.user;

import com.tpotato.codari.user.config.AppSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(AppSecurityProperties.class)
public class CodariUserWebappApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodariUserWebappApplication.class, args);
  }

}
