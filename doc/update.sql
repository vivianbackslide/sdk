CREATE TABLE `t_accusation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `reason_id` int(11) NOT NULL COMMENT '举报理由id',
  `content` varchar(220) NOT NULL DEFAULT '' COMMENT '举报描述',
  `pics` varchar(400) NOT NULL DEFAULT '' COMMENT '举报图片',
  `create_time` datetime NOT NULL COMMENT '举报时间',
  `user_id` bigint(20) NOT NULL COMMENT '举报人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `t_accusation_reason` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '类别标题',
  `group_id` int(11) NOT NULL DEFAULT '0' COMMENT '类别分组',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='举报类型';
CREATE TABLE `t_accusation_reason_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='举报类型分组表';
CREATE TABLE `t_accusation_relationship` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_id` int(11) NOT NULL COMMENT '举报类型0-视频1-评论2-用户',
  `reason_group` int(11) NOT NULL COMMENT '举报理由分组id',
  `reason_id` int(11) DEFAULT NULL COMMENT '举报理由',
  `sort` int(11) NOT NULL DEFAULT '1' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='举报理由类型关系表';

ALTER TABLE `db_qingke_zxx`.`t_feedback`
DROP COLUMN `nick_name`,
MODIFY COLUMN `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '反馈内容' AFTER `user_id`,
ADD COLUMN `pics` varchar(400) NOT NULL DEFAULT '' COMMENT '反馈图片' AFTER `create_time`,
ADD COLUMN `contact` varchar(50) NOT NULL DEFAULT '' COMMENT '联系方式' AFTER `pics`;

ALTER TABLE `db_qingke_zxx`.`t_vedio`
ADD COLUMN `city_address` varchar(30) NOT NULL DEFAULT '' COMMENT '具体地址' AFTER `aite`,
ADD COLUMN `positive_energy` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否正能量0-否1-是' AFTER `city_address`;

ALTER TABLE `db_qingke_zxx`.`t_user_main`
ADD COLUMN `authentication_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '认证情况，0-未认证 1-个人 ，2-企业 3-机构' AFTER `qingke_change_num`;

ALTER TABLE `db_qingke_zxx`.`t_user_friend`
ADD COLUMN `remark_name` varchar(20) NOT NULL DEFAULT '' COMMENT '备注名' AFTER `update_time`;
