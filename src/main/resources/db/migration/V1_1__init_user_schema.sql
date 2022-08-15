drop table if exists tb_codari_addr;
drop table if exists tb_codari;
drop table if exists tb_user_oauth;
drop table if exists tb_user_addr;
drop table if exists tb_user_phone;
drop table if exists tb_user_favorite_board;
drop table if exists tb_user;

create table `codari-user`.tb_user (
   `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
   `name` varchar(255) NOT NULL,
   `email` varchar(255) NOT NULL,
   `grade` char(1) DEFAULT NULL,

   `location_exposure` char(1) DEFAULT NULL,
   `profile_image` varchar(255) DEFAULT NULL,

   `created_datetime` datetime DEFAULT NULL,
   `update_datetime` datetime DEFAULT NULL,
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

    `created_datetime` datetime DEFAULT NULL,
    `update_datetime` datetime DEFAULT NULL,
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

     `created_datetime` datetime DEFAULT NULL,
     `update_datetime` datetime DEFAULT NULL,
     PRIMARY KEY (`user_addr_id`),
     FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);

create table `codari-user`.tb_user_phone (
    `user_phone_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL,

    `mobile_operator` varchar(255) NOT NULL,
    `phone_number` varchar(255) NOT NULL,

    `created_datetime` datetime DEFAULT NULL,
    `update_datetime` datetime DEFAULT NULL,
    PRIMARY KEY (`user_phone_id`),
    FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);


create table `codari-user`.tb_user_favorite_board (
     `user_favorite_board_id` bigint(20) NOT NULL AUTO_INCREMENT,
     `user_id` bigint(20) NOT NULL,

     `board_id` bigint(20) NOT NULL,
     `board_type` varchar(10) NOT NULL,

     `created_datetime` datetime DEFAULT NULL,
     `update_datetime` datetime DEFAULT NULL,
     PRIMARY KEY (`user_favorite_board_id`),
     UNIQUE (`board_id`, `board_type`),
     FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);


create table `codari-user`.tb_codari (
`codari_id` bigint(20) NOT NULL AUTO_INCREMENT,
`user_id` bigint(20) NOT NULL,

`codari_name` varchar(255) NOT NULL,

`created_datetime` datetime DEFAULT NULL,
`update_datetime` datetime DEFAULT NULL,
PRIMARY KEY (`codari_id`),
FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`)
);

create table `codari-user`.tb_codari_addr (
  `codari_addr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codari_id` bigint(20) NOT NULL,

  `first_addr` varchar(255) NOT NULL,
  `second_addr` varchar(255) NOT NULL,
  `third_addr` varchar(255) NOT NULL,
  `detail_addr` varchar(255) NOT NULL,

  `latitude` varchar(255) NOT NULL,
  `longitude` varchar(255) NOT NULL,

  `created_datetime` datetime DEFAULT NULL,
  `update_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`codari_addr_id`),
  FOREIGN KEY (`codari_id`) REFERENCES `tb_codari` (`codari_id`)
);


