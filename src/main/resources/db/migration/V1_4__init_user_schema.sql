drop table if exists tb_user_oauth;
drop table if exists tb_user_addr;
drop table if exists tb_user_phone;
drop table if exists tb_user_favorite_board;
drop table if exists tb_user;
drop table if exists tb_user_acquired_badge;
drop table if exists tb_badge_meta;

create table `codari-user`.tb_user (
   `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
   `codari_name` varchar(255) NOT NULL,
   `grade` char(1) DEFAULT NULL,

   `location_exposure` char(1) DEFAULT NULL,
   `profile_image` varchar(255) DEFAULT NULL,
   `email` varchar(255) NOT NULL,
   `additional_verification_code` varchar(10) DEFAULT NULL,

   `created_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
   `update_datetime` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`user_id`),
   UNIQUE KEY (`email`)
) ;


create table `codari-user`.tb_user_oauth (
    `user_oauth_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL,

    `vender` varchar(255) NOT NULL,
    `provider_id` varchar(255) NOT NULL,
    `user_name` varchar(255) NOT NULL,
    `user_profile_image` varchar(255) NOT NULL,
    `user_email` varchar(255) NOT NULL,

    `created_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_datetime` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_oauth_id`),
    UNIQUE KEY (`vender`, `provider_id`),
    FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);

create table `codari-user`.tb_user_addr (
     `user_addr_id` bigint(20) NOT NULL AUTO_INCREMENT,
     `user_id` bigint(20) NOT NULL,

     `first_addr` varchar(255) NOT NULL,
     `second_addr` varchar(255) NOT NULL,
     `third_addr` varchar(255) NOT NULL,
     `detail_addr` varchar(255) NOT NULL,

     `latitude` varchar(255) NOT NULL,
     `longitude` varchar(255) NOT NULL,

     `created_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
     `update_datetime` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (`user_addr_id`),
     FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);

create table `codari-user`.tb_user_phone (
    `user_phone_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL,

    `mobile_operator` varchar(255) NOT NULL,
    `phone_number` varchar(255) NOT NULL,

    `created_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_datetime` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_phone_id`),
    FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);

create table `codari-user`.tb_badge_meta (
      `badge_meta_id` bigint(20) NOT NULL AUTO_INCREMENT,
      `badge_name` varchar(50) NOT NULL,
      `desc` TEXT NULL,

      `created_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
      `update_datetime` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (`badge_meta_id`),
      UNIQUE KEY badge_name_index (badge_name)
);


create table `codari-user`.tb_user_acquired_badge (
     `user_acquired_badge_id` bigint(20) NOT NULL AUTO_INCREMENT,
     `user_id` bigint(20) NOT NULL,
     `badge_meta_id` bigint(20) NOT NULL,

     `created_datetime` DATETIME DEFAULT CURRENT_TIMESTAMP,
     `update_datetime` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (`user_acquired_badge_id`),
     FOREIGN KEY (`badge_meta_id`) REFERENCES `tb_badge_meta` (`badge_meta_id`),
     FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);
