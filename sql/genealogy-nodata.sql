/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : genealogy

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 22/07/2022 16:31:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dou_record
-- ----------------------------
DROP TABLE IF EXISTS `dou_record`;
CREATE TABLE `dou_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int NOT NULL COMMENT '用户id',
  `dou_amount` int NOT NULL COMMENT '领取的斗数量',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '斗收入明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for essay
-- ----------------------------
DROP TABLE IF EXISTS `essay`;
CREATE TABLE `essay`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `family_id` int NOT NULL COMMENT '首次发布的家族id(用来指定家族名)',
  `browse_number` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `praise_number` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `discuss_number` int NOT NULL DEFAULT 0 COMMENT '评论数',
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `address` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发帖地点',
  `open` tinyint(1) NOT NULL COMMENT '开放1保密2家族内公开3完全公开',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `is_knit` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否加锁0未加1已加',
  `knit_cycle` int NOT NULL DEFAULT 0 COMMENT '被锁周期（默认30天，可以充值米延长）',
  `knit_start_time` int NOT NULL DEFAULT 0 COMMENT '加锁倒计时初始日期（浏览点赞会刷新它）',
  `knit_end_time` int NOT NULL COMMENT '加锁倒计时结束日期（同上，并且到这个时间就加锁）',
  `auto_remove_time` int NOT NULL DEFAULT 0 COMMENT '自动删除日期（加锁后刷新）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 924 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '贴子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for essay_discuss
-- ----------------------------
DROP TABLE IF EXISTS `essay_discuss`;
CREATE TABLE `essay_discuss`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `essay_id` int NOT NULL COMMENT '文章id',
  `user_id` int NOT NULL COMMENT '用户id',
  `essay_discuss_id` int NOT NULL DEFAULT 0 COMMENT '文章评论id(二级评论需要)',
  `level` tinyint(1) NOT NULL COMMENT '等级：1一级评论2二级评论',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文章评论' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for essay_img
-- ----------------------------
DROP TABLE IF EXISTS `essay_img`;
CREATE TABLE `essay_img`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `essay_id` int NOT NULL COMMENT '关联文章id',
  `img` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1509 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帖子图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for essay_share
-- ----------------------------
DROP TABLE IF EXISTS `essay_share`;
CREATE TABLE `essay_share`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `essay_id` int NOT NULL,
  `family_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for essay_support
-- ----------------------------
DROP TABLE IF EXISTS `essay_support`;
CREATE TABLE `essay_support`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `essay_id` int NOT NULL COMMENT '文章id',
  `user_id` int NOT NULL COMMENT '用户id',
  `is_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '点赞状态0取消1确实',
  `create_time` datetime NOT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文章点赞' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for family
-- ----------------------------
DROP TABLE IF EXISTS `family`;
CREATE TABLE `family`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '创建者id(用户id)',
  `logo` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'logo',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '家族名称',
  `people_number` int NOT NULL DEFAULT 1 COMMENT '家族人数',
  `examine_number` int NOT NULL DEFAULT 0 COMMENT '审批消息',
  `mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `puname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '谱名',
  `hunname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '婚姻',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '家族' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for family_clean
-- ----------------------------
DROP TABLE IF EXISTS `family_clean`;
CREATE TABLE `family_clean`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL DEFAULT 0,
  `user_id` int NOT NULL DEFAULT 0 COMMENT '上一次打扫时间',
  `clean_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for family_genealogy
-- ----------------------------
DROP TABLE IF EXISTS `family_genealogy`;
CREATE TABLE `family_genealogy`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL COMMENT '家族id',
  `family_user_id` int NULL DEFAULT NULL COMMENT '家族成员id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `relation` tinyint(1) NOT NULL COMMENT '和家族关系1直亲2婚姻3表亲',
  `identity` tinyint(1) NOT NULL COMMENT '身份：1：直系，2：婚姻',
  `generation` int NOT NULL COMMENT '第几代',
  `genealogy_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '家谱名',
  `sex` tinyint(1) NOT NULL COMMENT '性别1男2女',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `family_lian_id` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 723 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '家谱图' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for family_genealogy_copy1
-- ----------------------------
DROP TABLE IF EXISTS `family_genealogy_copy1`;
CREATE TABLE `family_genealogy_copy1`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL COMMENT '家族id',
  `family_user_id` int NULL DEFAULT NULL COMMENT '家族成员id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `relation` tinyint(1) NOT NULL COMMENT '和家族关系1直亲2婚姻3表亲',
  `identity` tinyint(1) NOT NULL COMMENT '身份：1：直系，2：婚姻',
  `generation` int NOT NULL COMMENT '第几代',
  `genealogy_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '家谱名',
  `sex` tinyint(1) NOT NULL COMMENT '性别1男2女',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '家谱图' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for family_manage_log
-- ----------------------------
DROP TABLE IF EXISTS `family_manage_log`;
CREATE TABLE `family_manage_log`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL COMMENT '家族id',
  `family_user_id` int NOT NULL COMMENT '家族成员id',
  `user_id` int NOT NULL COMMENT '用户id',
  `action` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '行为',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '家族管理日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for family_message
-- ----------------------------
DROP TABLE IF EXISTS `family_message`;
CREATE TABLE `family_message`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL DEFAULT 0,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `essay_message` int NOT NULL DEFAULT 0 COMMENT '帖子消息',
  `familyuser_message` int NOT NULL DEFAULT 0 COMMENT '家族成员信息',
  `familyshen_message` int NOT NULL DEFAULT 0 COMMENT '审批消息',
  `familymanage_message` int NOT NULL DEFAULT 0 COMMENT '管理员消息',
  `familylog_message` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for family_user
-- ----------------------------
DROP TABLE IF EXISTS `family_user`;
CREATE TABLE `family_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL COMMENT '家族id',
  `user_id` int NOT NULL DEFAULT 0 COMMENT '用户id',
  `level` tinyint(1) NOT NULL COMMENT '身份等级：1是创建者2是管理员3是会员',
  `generation` int NULL DEFAULT NULL COMMENT '第几代',
  `genealogy_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家谱名',
  `relation` tinyint(1) NULL DEFAULT NULL COMMENT '和家族关系1直亲2婚姻3表亲',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别1男2女',
  `status` tinyint(1) NOT NULL COMMENT '申请状态：1申请中2已通过3已拒绝',
  `introduce` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请说明',
  `remarks` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '日期',
  `message_number` int NOT NULL DEFAULT 0 COMMENT '新消息数',
  `manage_time` datetime NULL DEFAULT NULL COMMENT '成为管理员日期',
  `update_time` datetime NULL DEFAULT NULL,
  `updatestatus` int NOT NULL DEFAULT 0 COMMENT '1表示完成，0表示没有',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '家族用户关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for rice_record
-- ----------------------------
DROP TABLE IF EXISTS `rice_record`;
CREATE TABLE `rice_record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `rice` int NOT NULL COMMENT '米',
  `content` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '明细内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2159 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '米收支明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `operation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作',
  `method` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '方法名',
  `params` varchar(2047) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户IP',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单标识',
  `create_by_id` int UNSIGNED NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by_id` int NOT NULL COMMENT '修改人ID',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态(1：正常  2：冻结 ）',
  `del_flag` int NOT NULL DEFAULT 1 COMMENT '删除状态(1：正常  2：删除 ）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `del_flag`) USING BTREE,
  INDEX `status`(`status`, `del_flag`) USING BTREE,
  INDEX `del_flag`(`del_flag`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `permission` varchar(63) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限标识',
  `name` varchar(63) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名',
  `create_by_id` int NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by_id` int NOT NULL COMMENT '修改人ID',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `permission`(`permission`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
  `create_by_id` int NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by_id` int NOT NULL COMMENT '修改人ID',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int NOT NULL COMMENT '角色ID',
  `menu_id` int NOT NULL COMMENT '菜单ID',
  `create_by_id` int NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int NOT NULL COMMENT '角色ID',
  `permission_id` int NOT NULL COMMENT '权限ID',
  `create_by_id` int NOT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 368 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `role_id` int NOT NULL COMMENT '角色ID',
  `create_by_id` int NOT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `head_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像链接',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `created_by_id` int NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by_id` int NOT NULL COMMENT '修改人ID',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态(1：正常  2：冻结 ）',
  `del_flag` int NOT NULL DEFAULT 1 COMMENT '删除状态(1：正常  2：删除 ）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t1
-- ----------------------------
DROP TABLE IF EXISTS `t1`;
CREATE TABLE `t1`  (
  `id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t2
-- ----------------------------
DROP TABLE IF EXISTS `t2`;
CREATE TABLE `t2`  (
  `id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `titile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务规则',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '任务描述',
  `img_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片多个逗号隔开',
  `reward_mi` int NOT NULL COMMENT '奖励米',
  `reward_dou` int NOT NULL COMMENT '奖励斗',
  `user_id` int NOT NULL COMMENT '发布人',
  `get_number` int NOT NULL DEFAULT 0 COMMENT '领取人数',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否可领取（0否1是，默认1）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `deadline_time` datetime NOT NULL COMMENT '领取截止时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for task_user
-- ----------------------------
DROP TABLE IF EXISTS `task_user`;
CREATE TABLE `task_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_id` int NOT NULL COMMENT '任务id',
  `user_id` int NOT NULL COMMENT '领取人id',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否完成（0执行中/1待审核/2完成3拒绝  默认0）',
  `results_img_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '完成截图（多张逗号隔开）',
  `refuse_why` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `create_time` datetime NOT NULL COMMENT '领取时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tombstone
-- ----------------------------
DROP TABLE IF EXISTS `tombstone`;
CREATE TABLE `tombstone`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `family_id` int NOT NULL DEFAULT 0,
  `familyuser_id` int NOT NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT NULL,
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tombstone_essay
-- ----------------------------
DROP TABLE IF EXISTS `tombstone_essay`;
CREATE TABLE `tombstone_essay`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tombstone_id` int NOT NULL DEFAULT 0,
  `essay_id` int NOT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tombstone_flower
-- ----------------------------
DROP TABLE IF EXISTS `tombstone_flower`;
CREATE TABLE `tombstone_flower`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tombstone_id` int NOT NULL DEFAULT 0,
  `user_id` int NOT NULL DEFAULT 0,
  `flower_num` int NOT NULL DEFAULT 0 COMMENT '献花次数',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tombstone_gift
-- ----------------------------
DROP TABLE IF EXISTS `tombstone_gift`;
CREATE TABLE `tombstone_gift`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '贡品名称',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '贡品图片',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT NULL,
  `tombstone_id` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '贡品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tombstone_message
-- ----------------------------
DROP TABLE IF EXISTS `tombstone_message`;
CREATE TABLE `tombstone_message`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tombstone_id` int NOT NULL DEFAULT 0,
  `user_id` int NOT NULL DEFAULT 0,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tombstone_sweep
-- ----------------------------
DROP TABLE IF EXISTS `tombstone_sweep`;
CREATE TABLE `tombstone_sweep`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tombstone_id` int NOT NULL DEFAULT 0,
  `user_id` int NOT NULL DEFAULT 0,
  `sweep_num` int NOT NULL DEFAULT 0 COMMENT '扫墓次数',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tombstone_usergift
-- ----------------------------
DROP TABLE IF EXISTS `tombstone_usergift`;
CREATE TABLE `tombstone_usergift`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `gift_id` int NOT NULL,
  `user_id` int NOT NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT NULL,
  `tombstone_id` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-献祭表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for trade
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `out_trade_no` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户订单号,长度6-31',
  `amount_total` int UNSIGNED NOT NULL COMMENT '订单总金额，单位为分',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态0待支付1已支付',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信支付订单号',
  `rice` int NOT NULL DEFAULT 0 COMMENT '米',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_out_trade_no`(`out_trade_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_trade_state`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `openid` char(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'openid',
  `avatar` varchar(140) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信昵称',
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `idcard_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `idcard_front_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证正面图',
  `idcard_behind_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证背面',
  `is_certification` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否实名认证（0待审核1通过 默认0）',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别1男2女',
  `rice` int NOT NULL DEFAULT 0 COMMENT '米',
  `dou` int NOT NULL DEFAULT 0 COMMENT '斗',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `second_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `clean` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 289 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_family_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_family_follow`;
CREATE TABLE `user_family_follow`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `family_id` int NOT NULL COMMENT '家族id',
  `is_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：1已关注0未关注',
  `message_number` int NOT NULL DEFAULT 0 COMMENT '新消息数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户关注家族' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
