DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`
(
    `id`          int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `username`    varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
    `password`    varchar(64) NOT NULL DEFAULT '' COMMENT '密码',
    `sex`         tinyint(4) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0女1男',
    `deleted`     tinyint(4) UNSIGNED NOT NULL DEFAULT '0' COMMENT '0删除',
    `update_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;