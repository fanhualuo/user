CREATE TABLE `us_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(40) DEFAULT NULL COMMENT '用户名',
  `email` varchar(254) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '加密密码',
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  `type` int(2) NOT NULL DEFAULT '1',
  `avatar_url` varchar(2000) DEFAULT NULL COMMENT '头像url',
  `created_at` datetime DEFAULT NULL ,
  `updated_at` datetime DEFAULT NULL ,
  PRIMARY KEY (`id`),
  key `idx_username` (`username`),
  key `idx_email` (`email`),
  key `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

CREATE TABLE `us_users_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `info_detailed` text DEFAULT NULL COMMENT '用户详细信息、json字符串',
  `created_at` datetime DEFAULT NULL ,
  `updated_at` datetime DEFAULT NULL ,
  PRIMARY KEY (`id`),
  key `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息明细表';




CREATE TABLE `us_clients` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` varchar(128) NOT NULL,
  `client_name` varchar(100) DEFAULT NULL COMMENT '应用名',
  `client_secret` varchar(100) DEFAULT NULL COMMENT '应用户名',
  `client_url` varchar(2000) DEFAULT NULL COMMENT '应用地址',
  `client_logo_url` varchar(2000) DEFAULT NULL COMMENT '应用logo',
  `registered_redirect_uri` varchar(6000) DEFAULT NULL COMMENT '回跳地址',
  `authorized_grant_types` varchar(6000) DEFAULT NULL COMMENT '授权类型',
  `resource_ids` varchar(128) DEFAULT NULL COMMENT '资源id',
  `scope` varchar(1024) DEFAULT NULL COMMENT '范围',
  `access_token_validity_seconds` bigint(20) DEFAULT NULL,
  `refresh_token_validity_seconds` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL ,
  `updated_at` datetime DEFAULT NULL ,
  PRIMARY KEY (`id`),
    key `idx_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用信息表';


-- code存储表
CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob DEFAULT NULL,
   key `idx_authentication` (`authentication`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='code存储表';