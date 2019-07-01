ALTER TABLE `db_qingke_zxx`.`t_verify_code`
ADD COLUMN `send_ip` varchar(15) NOT NULL DEFAULT '' COMMENT '发送IP' AFTER `create_time`;

ALTER TABLE `db_qingke_zxx`.`t_vedio`
MODIFY COLUMN `id` bigint(30) NOT NULL AUTO_INCREMENT COMMENT '主键id' FIRST;

ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD COLUMN `content` varchar(255) NOT NULL DEFAULT '' COMMENT '发布的内容' AFTER `topic`;
*-*
+
ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD COLUMN `city_id` int(0) NULL COMMENT '城市ID，视频发布的城市' AFTER `content`;

ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD COLUMN `visible_to` tinyint(1) NOT NULL DEFAULT 0 COMMENT '对谁可见 0-公开 1-好友 2-私密' AFTER `city_id`,
ADD INDEX `visible_unique_index`(`visible_to`) USING BTREE;

--20190509
ALTER TABLE `db_qingke_zxx`.`t_user_likes`
ADD UNIQUE INDEX `user_vedio_unique_idx`(`user_id`, `vedio_id`) USING BTREE COMMENT '唯一索引';

--20190510
ALTER TABLE `db_qingke_zxx`.`t_user_main`
MODIFY COLUMN `total_likes_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '总获赞数' AFTER `fans_count`;

CREATE TABLE `t_user_friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '自己的id',
  `friend_id` bigint(20) NOT NULL COMMENT '朋友id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_friend_unique_idx` (`user_id`,`friend_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='朋友表';


CREATE TABLE `t_user_focus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT ' 关注人',
  `focus_id` bigint(20) NOT NULL COMMENT '被关注人',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_focus_unique_idx` (`user_id`,`focus_id`) USING BTREE COMMENT '用户关注唯一'
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='我的关注';

CREATE TABLE `t_user_fans` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '自己id',
  `fans_id` bigint(20) NOT NULL COMMENT '粉丝id',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_fans_unique_idx` (`user_id`,`fans_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='我的粉丝';

--20190513
ALTER TABLE `db_qingke_zxx`.`t_comment`
DROP COLUMN `level`,
DROP COLUMN `comment_path`,
MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id' FIRST,
MODIFY COLUMN `vedio_id` bigint(20) NOT NULL COMMENT '被评论的视频id' AFTER `id`,
MODIFY COLUMN `user_id` bigint(20) NOT NULL COMMENT '评论人id' AFTER `vedio_id`,
MODIFY COLUMN `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '被评论的视频id,或者评论的id' AFTER `examine`,
ADD COLUMN `reply_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '回复id' AFTER `parent_id`,
ADD COLUMN `reply_nick` varchar(20) NOT NULL DEFAULT '' COMMENT '回复人昵称' AFTER `reply_id`,
ADD COLUMN `aite` varchar(255) NOT NULL DEFAULT '' COMMENT '艾特的的人，格式“[{\"id\":12,\"name\":\"虎哥\"}]”' AFTER `reply_nick`;

ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD COLUMN `comment_count` int(11) NOT NULL DEFAULT 0 COMMENT '评论数量' AFTER `watch_count`;

--20190515
CREATE TABLE `t_user_comment_thumsup` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `vedio_id` bigint(20) NOT NULL COMMENT '视频id',
  `comment_id` bigint(20) NOT NULL COMMENT '点赞的评论id',
  PRIMARY KEY (`id`),
  KEY `user_vedio_nomar_idx` (`user_id`,`vedio_id`) USING BTREE COMMENT '索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='我的评论点赞';

ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD COLUMN `popular_sroce` int(11) NOT NULL DEFAULT 0 COMMENT '上热门分数' AFTER `music_id`;
ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD COLUMN `is_popular` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否热门视频，0-否，1-是' AFTER `popular_sroce`;

CREATE TABLE `t_ad_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `weight` int(11) NOT NULL DEFAULT '0' COMMENT '权重',
  `rule_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型：置顶，上热门，前置显示',
  `title` varchar(40) NOT NULL DEFAULT '' COMMENT '规则名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告规则';

CREATE TABLE `t_popular_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `weight` int(11) NOT NULL DEFAULT '0' COMMENT '权重',
  `rule_type` tinyint(4) NOT NULL COMMENT '类型',
  `title` varchar(40) NOT NULL DEFAULT '' COMMENT '标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='上热门规则';

INSERT INTO `t_system_config`(`key`, `value`, `remark`) VALUES ('popular.vedio.score', '100', '上热门的分数');
INSERT INTO `t_system_config`(`key`, `value`, `remark`) VALUES ('popular.vedio.max.cache.count', '20', '缓存热门视频数量');
INSERT INTO `t_system_config`(`key`, `value`, `remark`) VALUES ('popular.vedio.max.cache.day', '5', '缓存热门视频天数');
INSERT INTO `t_system_config`(`key`, `value`, `remark`) VALUES ('qiniu.Bucket', 'vedio', '七牛的空间');

--20190517
INSERT INTO `db_qingke_zxx_conf`.`t_system_config`( `key`, `value`, `remark`) VALUES ('image.host', 'http://prjom5a8f.bkt.clouddn.com/', '七牛的资源地址');

--20190519
ALTER TABLE `db_qingke_zxx`.`t_vedio_ad`
CHANGE COLUMN `resend_count` `forward_count` int(11) NOT NULL DEFAULT 0 COMMENT '转发数' AFTER `likes_count`,
MODIFY COLUMN `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '所属' AFTER `id`,
MODIFY COLUMN `vedio_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题' AFTER `user_id`,
MODIFY COLUMN `vedio_url` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频地址' AFTER `vedio_title`,
MODIFY COLUMN `vedio_link_address` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '点击跳转的地方' AFTER `vedio_url`;
ALTER TABLE `db_qingke_zxx`.`t_vedio_ad`
CHANGE COLUMN `weight _level` `weight_level` int(11) NOT NULL DEFAULT 0 COMMENT '权重等级，数值越小权重越高,取值范围（1-100）' AFTER `watch_count`;

ALTER TABLE `db_qingke_zxx`.`t_vedio_ad`
ADD COLUMN `comment_count` int(11) NOT NULL DEFAULT 0 COMMENT '评论数量' AFTER `watch_count`;

--20190523
CREATE TABLE `t_user_dunamic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `vedio_id` bigint(20) NOT NULL COMMENT '被转发的视频',
  `content` varchar(200) DEFAULT '' COMMENT '转发说的文字',
  `thumbsup_count` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_foward` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否转发0-否 1-是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='我的动态';

drop table if exists `t_user_message`;
CREATE TABLE `t_user_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `from_id` bigint(20) NOT NULL COMMENT '发送人id',
  `vedio_id` bigint(20) NOT NULL COMMENT '视频id',
  `comment_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '评论id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `msg_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消息类型，0-点赞 1-评论 2-@',
  `work_type` tinyint(4) NOT NULL COMMENT '消息体内容 0-作品 1-转发 2-评论',
  `read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读 0-否1-是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 20190529
INSERT INTO `db_qingke_zxx_conf`.`t_system_config`( `key`, `value`, `remark`) VALUES ('tencent.im.sdkappid', '1400036818', '腾讯im的appid');
INSERT INTO `db_qingke_zxx_conf`.`t_system_config`( `key`, `value`, `remark`) VALUES ('tencent.im.identifier', 'qingke', '腾讯IM管理员账号');
INSERT INTO `db_qingke_zxx_conf`.`t_system_config`( `key`, `value`, `remark`) VALUES ('tencent.im.primarykey', '-----BEGIN PRIVATE KEY-----\r\nMIGEAgEAMBAGByqGSM49AgEGBSuBBAAKBG0wawIBAQQgai9mA04v1t+oSvU2b1fVC8mP8/S/E3NwBufQsJ7VcGGhRANCAAT/Yk1C2/E84OF4oC812YarA2t66MRbNPUJwIhmxZy4sgO+ML6sz2zYcrMttOK8ODrXcK4DRkZ+Me7cLfOm2Jzq\r\n-----END PRIVATE KEY-----', '腾讯IM私钥');
INSERT INTO `db_qingke_zxx_conf`.`t_system_config`( `key`, `value`, `remark`) VALUES ( 'app.version', '1.0', 'app的版本号');
INSERT INTO `db_qingke_zxx_conf`.`t_system_config`(`key`, `value`, `remark`) VALUES ( 'image.host', 'http://prjom5a8f.bkt.clouddn.com/', '七牛的资源地址');
INSERT INTO `db_qingke_zxx_conf`.`t_system_config`( `key`, `value`, `remark`) VALUES ( 'index.pic.url', 'http://pic15.nipic.com/20110628/1369025_192645024000_2.jpg', '首页的地址');
