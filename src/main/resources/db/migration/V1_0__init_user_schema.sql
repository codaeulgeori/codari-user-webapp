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

drop table if exists tb_user_oauth;
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