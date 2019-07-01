--20190603
CREATE TABLE `t_notice_thumbsup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '所属用户',
  `comment_id` bigint(20) NOT NULL COMMENT '评论的视频或者评论id，@的来源视频id',
  `op_type` tinyint(4) NOT NULL COMMENT '消息类型,0-作品 1-评论',
  `is_read` tinyint(1) NOT NULL COMMENT '状态0-未读 1-已读',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `vedio_url` varchar(255) NOT NULL COMMENT '视频地址',
  `last_thumbsup` varchar(255) DEFAULT NULL COMMENT '最近点赞的5个',
  `is_friend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否朋友操作 0-否 1-是',
  `vedio_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '视频id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户收到的点赞信息';

ALTER TABLE `t_notice_thumbsup`
ADD COLUMN `is_friend` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否朋友操作 0-否 1-是' AFTER `last_thumbsup`;
ALTER TABLE `t_notice_thumbsup`
ADD COLUMN `vedio_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '视频id' AFTER `is_friend`;
ALTER TABLE `t_notice_comment`
ADD COLUMN `vedio_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '视频id' AFTER `op_type`;
ALTER TABLE `t_vedio`
ADD COLUMN `cover_url` varchar(255) NOT NULL DEFAULT '' COMMENT '封面路径' AFTER `is_popular`;
-- 20190605
ALTER TABLE `t_notice_thumbsup`
ADD COLUMN `update_time` datetime(0) NOT NULL COMMENT '最后更新时间' AFTER `vedio_id`;
