/*
 Navicat Premium Data Transfer

 Source Server         : imaboss
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : db.imaboss.cn:3306
 Source Schema         : project

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 23/11/2021 17:49:44
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm`;
CREATE TABLE `sys_perm`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限id',
    `sp_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
    `sp_key`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
    `sp_remark`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
    `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `delete_date` datetime NULL DEFAULT NULL COMMENT '删除时间',
    `deleted`     tinyint(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `ux_perms_key`(`sp_key`) USING BTREE COMMENT '权限key唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_perm
-- ----------------------------
INSERT INTO `sys_perm`
VALUES ('1', '查看用户权限', 'system:user:list', '', '2020-11-02 09:16:31', NULL, NULL, 0);
INSERT INTO `sys_perm`
VALUES ('2', '更新用户权限', 'system:user:update', '', '2020-11-02 09:16:31', NULL, NULL, 0);
INSERT INTO `sys_perm`
VALUES ('3', '删除用户权限', 'system:user:remove', '', '2020-11-02 09:16:31', NULL, NULL, 0);
INSERT INTO `sys_perm`
VALUES ('4', '添加用户权限', 'system:user:insert', '', '2020-11-02 09:16:31', NULL, NULL, 0);
INSERT INTO `sys_perm`
VALUES ('5', '用户所有权限', 'system:user:*', '', '2020-11-02 09:16:31', NULL, NULL, 0);
INSERT INTO `sys_perm` (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('6', '上传附件', 'attachment:insert', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO `sys_perm` (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('7', '下载附件', 'attachment:get', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO `sys_perm` (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('8', '删除附件', 'attachment:remove', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO `sys_perm` (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('9', '附件所有权限', 'attachment:*', '', '2020-11-02 09:16:31', null, null, 0);


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色id',
    `sr_name`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
    `sr_key`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
    `sr_remark`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
    `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `delete_date` datetime NULL DEFAULT NULL COMMENT '删除时间',
    `deleted`     tinyint(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `ux_role_key`(`sr_key`) USING BTREE COMMENT '角色权限key唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES ('1', '管理员', 'admin', '管理员', '2020-06-22 08:58:26', '2020-06-22 08:58:26', NULL, 0);
INSERT INTO `sys_role`
VALUES ('2', '普通角色', 'common', '普通角色', '2020-06-22 08:58:26', '2020-06-22 08:58:26', NULL, 0);

-- ----------------------------
-- Table structure for sys_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_perm`;
CREATE TABLE `sys_role_perm`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
    `role_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
    `perm_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限ID',
    `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `role_id`(`role_id`, `perm_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_perm
-- ----------------------------
INSERT INTO `sys_role_perm`
VALUES ('', '1', '5', '2021-11-23 17:49:28');
INSERT INTO `sys_role_perm` (id, role_id, perm_id, create_date)
VALUES ('2', '1', '9', '2023-01-20 19:41:07');


-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
    `su_phone`    char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NOT NULL COMMENT '手机号',
    `su_salt`     char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NOT NULL COMMENT '盐值',
    `su_password` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NOT NULL COMMENT '密码',
    `su_avatar`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
    `su_name`     char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
    `su_sex`      tinyint(1) NULL DEFAULT 0 COMMENT '性别0男1女',
    `su_birth`    date NULL DEFAULT NULL COMMENT '出生日期',
    `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `delete_date` datetime NULL DEFAULT NULL COMMENT '删除时间',
    `deleted`     tinyint(1) NULL DEFAULT 0 COMMENT '0未删除1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `phone`(`su_phone`) USING BTREE COMMENT '手机号唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES ('1', '18735182285', 'f0d1503610f54898b38280740b7bd42e',
        '1ada0549b879804c95ed65d28168b5fdec894aa770cc5921b39a3d86dd0af3ee',
        'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg', 'sweetBaby', 0, '1997-05-12',
        '2020-05-06 13:00:31', '2020-05-06 13:01:25', NULL, 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
    `user_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
    `role_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
    `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_role_id_user_id`(`user_id`, `role_id`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES ('1', '1', '1', '2021-11-23 17:29:53');

SET
FOREIGN_KEY_CHECKS = 1;
