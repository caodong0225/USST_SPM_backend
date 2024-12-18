/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80038 (8.0.38)
 Source Host           : localhost:3306
 Source Schema         : spm

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 18/12/2024 19:20:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcements
-- ----------------------------
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE `announcements`  (
                                  `id` int NOT NULL COMMENT '主键',
                                  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
                                  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '公告内容',
                                  `creator_id` int NOT NULL COMMENT '创建者id',
                                  `course_id` int NOT NULL COMMENT '课程id',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `announcements_courses_id_fk`(`course_id` ASC) USING BTREE,
                                  INDEX `announcements_users_id_fk`(`creator_id` ASC) USING BTREE,
                                  CONSTRAINT `announcements_courses_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                                  CONSTRAINT `announcements_users_id_fk` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for course_participants
-- ----------------------------
DROP TABLE IF EXISTS `course_participants`;
CREATE TABLE `course_participants`  (
                                        `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `course_id` int NOT NULL COMMENT '课程id',
                                        `user_id` int NOT NULL COMMENT '用户id',
                                        `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'normal' COMMENT '用户状态',
                                        `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `course_participants_courses_id_fk`(`course_id` ASC) USING BTREE,
                                        INDEX `course_participants_users_id_fk`(`user_id` ASC) USING BTREE,
                                        CONSTRAINT `course_participants_courses_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                                        CONSTRAINT `course_participants_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程的参与者' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
                            `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `course_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
                            `course_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '课程描述',
                            `course_pic` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '课程图片',
                            `start_time` timestamp NOT NULL COMMENT '课程开始时间',
                            `end_time` timestamp NOT NULL COMMENT '课程结束时间',
                            `status` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程当前状态',
                            `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `user_id` int NOT NULL COMMENT '开设该课程的用户',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `courses_users_id_fk`(`user_id` ASC) USING BTREE,
                            CONSTRAINT `courses_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '所开设课程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
                                  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子标题',
                                  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '帖子内容',
                                  `user_id` int NOT NULL COMMENT '帖子的接受用户的id',
                                  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `notifications_is_read_index`(`is_read` ASC) USING BTREE,
                                  INDEX `notifications_users_id_fk`(`user_id` ASC) USING BTREE,
                                  CONSTRAINT `notifications_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for paper_questions
-- ----------------------------
DROP TABLE IF EXISTS `paper_questions`;
CREATE TABLE `paper_questions`  (
                                    `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `paper_id` int NOT NULL COMMENT '试卷id',
                                    `question_id` int NULL DEFAULT NULL COMMENT '试题id',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `paper_questions_papers_id_fk`(`paper_id` ASC) USING BTREE,
                                    INDEX `paper_questions_questions_id_fk`(`question_id` ASC) USING BTREE,
                                    CONSTRAINT `paper_questions_papers_id_fk` FOREIGN KEY (`paper_id`) REFERENCES `papers` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
                                    CONSTRAINT `paper_questions_questions_id_fk` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '试卷的问题列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for papers
-- ----------------------------
DROP TABLE IF EXISTS `papers`;
CREATE TABLE `papers`  (
                           `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `paper_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '试卷名称',
                           `paper_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '试卷描述',
                           `paper_start_time` timestamp NULL DEFAULT NULL COMMENT '试卷开始时间',
                           `paper_end_time` timestamp NULL DEFAULT NULL COMMENT '试卷结束时间',
                           `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '试卷状态',
                           `visible` tinyint NOT NULL DEFAULT 1 COMMENT '是否公开',
                           `course_id` int NOT NULL COMMENT '课程id',
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `papers_courses_id_fk`(`course_id` ASC) USING BTREE,
                           CONSTRAINT `papers_courses_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '试卷信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `question_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题类型',
                              `question_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题题面',
                              `question_level` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问题难度级别',
                              `question_options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '问题选项',
                              `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `course_id` int NULL DEFAULT NULL COMMENT '所属课程',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `questions_courses_id_fk`(`course_id` ASC) USING BTREE,
                              CONSTRAINT `questions_courses_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '问题列表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_exts
-- ----------------------------
DROP TABLE IF EXISTS `user_exts`;
CREATE TABLE `user_exts`  (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `user_id` int NOT NULL COMMENT '用户id',
                              `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '键名',
                              `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '键值',
                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE INDEX `idx_user_key`(`key` ASC) USING BTREE,
                              INDEX `user_exts_users_id_fk`(`user_id` ASC) USING BTREE,
                              CONSTRAINT `user_exts_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户额外信息的值' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
                               `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `user_id` int NULL DEFAULT NULL COMMENT '用户的id主键',
                               `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE INDEX `idx_role_name`(`role_name` ASC) USING BTREE,
                               INDEX `user_roles_users_id_fk`(`user_id` ASC) USING BTREE,
                               CONSTRAINT `user_roles_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户的角色权限控制表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
                          `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
                          `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
                          `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户电话号码',
                          `hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码的哈希值',
                          PRIMARY KEY (`id`) USING BTREE,
                          UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE,
                          UNIQUE INDEX `idx_email`(`email` ASC) USING BTREE,
                          UNIQUE INDEX `idx_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
