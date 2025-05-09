CREATE TABLE `family_genealogy_receive`
(
    `id`                  int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `family_genealogy_id` int(11) NOT NULL COMMENT '认领的族谱图id',
    `user_id`             int(11) NOT NULL COMMENT '认领人id',
    `user_img`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
    `user_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名字',
    `status`              tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0待审核 1审核通过 2拒绝）',
    `apply_remark`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请备注',
    `refuse`              varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拒绝原因',
    `create_time`         datetime                                                      NOT NULL COMMENT '申请时间',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '修改时间',
    `family_id`           int(11) NOT NULL COMMENT '家族id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '认领族谱图申请' ROW_FORMAT = Dynamic;
