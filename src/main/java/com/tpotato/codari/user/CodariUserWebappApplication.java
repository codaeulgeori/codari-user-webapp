package com.tpotato.codari.user;

import com.tpotato.codari.user.config.AppSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppSecurityProperties.class)
public class CodariUserWebappApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodariUserWebappApplication.class, args);
  }

}
