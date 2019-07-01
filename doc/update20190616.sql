ALTER TABLE `t_music`
ADD COLUMN `music_style` tinyint(4) NOT NULL DEFAULT 0 COMMENT '音乐风格0-普通1-流行' AFTER `create_time`;

ALTER TABLE `t_vedio`
DROP COLUMN `vedio_type`,
MODIFY COLUMN `music_id` int(11) NOT NULL DEFAULT 0 COMMENT '音乐id' AFTER `visible_to`,
ADD COLUMN `music_url` varchar(100) NOT NULL DEFAULT '' COMMENT '音乐文件名' AFTER `music_id`,
ADD COLUMN `music_name` varchar(100) NOT NULL DEFAULT '' COMMENT '音乐名' AFTER `music_url`,
ADD COLUMN `music_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '音乐作者id' AFTER `music_name`,
ALTER TABLE `t_vedio`
ADD COLUMN `original` tinyint(2) NOT NULL DEFAULT 0 COMMENT '音乐来源 0-原创 1-他人原创2-音乐库' AFTER `music_user_id`,
ADD COLUMN `duration` int(11) NOT NULL DEFAULT 0 COMMENT '视频或音乐时长' AFTER `cover_url`,
MODIFY COLUMN `cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '封面路径' AFTER `duration`;

ALTER TABLE `t_music` 
DROP COLUMN `music_type`,
MODIFY COLUMN `create_time` datetime(0) NOT NULL COMMENT '创建时间' AFTER `music_style`;

ALTER TABLE `db_qingke_zxx`.`t_music`
MODIFY COLUMN `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户id' AFTER `id`,
ADD COLUMN `music_head` varchar(100) NOT NULL DEFAULT '' COMMENT '音乐头像' AFTER `music_name`,
MODIFY COLUMN `music_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '音乐路径' AFTER `use_count`;


ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD INDEX `music_url_index`(`music_url`) USING BTREE COMMENT '音乐文件名索引';

ALTER TABLE `db_qingke_zxx`.`t_user_main`
MODIFY COLUMN `nick_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户昵称' AFTER `user_type_id`,
MODIFY COLUMN `gender` tinyint(2) NOT NULL DEFAULT 0 COMMENT '性别 0:未知, 1-男，2-女' AFTER `birthday`,
MODIFY COLUMN `signature` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '个性签名' AFTER `gender`,
MODIFY COLUMN `head_image` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户头像(存相对路径)' AFTER `score`,
MODIFY COLUMN `login_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后一次登陆ip' AFTER `is_online`;

ALTER TABLE `db_qingke_zxx`.`t_vedio`
MODIFY COLUMN `content` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '发布的内容' AFTER `topic`,
MODIFY COLUMN `city_id` int(11) NULL DEFAULT 0 COMMENT '城市ID，视频发布的城市' AFTER `content`,
ADD COLUMN `aite` varchar(255) NOT NULL DEFAULT '' COMMENT '艾特的人格式：[{\"id\":12,\"name\":\"改动\"}]' AFTER `cover_url`;


