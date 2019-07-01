ALTER TABLE `db_qingke_zxx`.`t_user_collection`
CHANGE COLUMN `vedio_id` `collection_id` bigint(20) NOT NULL COMMENT '收藏的id' AFTER `user_id`,
MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id' FIRST,
MODIFY COLUMN `user_id` bigint(20) NOT NULL COMMENT '用户id' AFTER `id`,
ADD COLUMN `collection_type` tinyint(4) NOT NULL COMMENT '收藏类型' AFTER `collection_id`;

ALTER TABLE `db_qingke_zxx`.`t_user_collection`
ADD COLUMN `collection_url` varchar(200) NOT NULL DEFAULT '' COMMENT '收藏的路径' AFTER `collection_type`;
