-- 20190610
ALTER TABLE `db_qingke_zxx`.`t_user_fans`
ADD COLUMN `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读0-未读1-已读' AFTER `update_time`;

INSERT INTO `db_qingke_zxx_conf`.`t_system_config`( `key`, `value`, `remark`) VALUES ('wechat.userinfo.url', 'https://api.weixin.qq.com/sns/userinfo', '微信获取用户信息地址');
CREATE TABLE `t_music` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `music_name` varchar(100) NOT NULL COMMENT '音乐名称',
  `use_count` int(11) NOT NULL DEFAULT '0' COMMENT '使用次数',
  `music_url` varchar(255) NOT NULL DEFAULT '' COMMENT '音乐路径',
  `music_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '音乐类型 0-原创  1-导入',
  `duration` int(11) NOT NULL DEFAULT '0' COMMENT '时长',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='音乐信息表';

CREATE TABLE `t_index_ad` (
  `id` int(11) NOT NULL COMMENT '主键',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `ad_image` varchar(200) NOT NULL DEFAULT '' COMMENT '图片地址',
  `link_adr` varchar(200) NOT NULL DEFAULT '' COMMENT '要跳转的地址',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0-无效 1-有效',
  `level` int(8) NOT NULL DEFAULT '0' COMMENT '等级，等级越高越排在前',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='首页广告url';
