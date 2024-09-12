/*
 Navicat Premium Data Transfer

 Source Server         : 本地-mySQL
 Source Server Type    : MySQL
 Source Server Version : 50737 (5.7.37)
 Source Host           : 192.168.0.105:3306
 Source Schema         : xxl_job

 Target Server Type    : MySQL
 Target Server Version : 50737 (5.7.37)
 File Encoding         : 65001

 Date: 11/09/2024 17:48:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xxl_job_group
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_group`;
CREATE TABLE `xxl_job_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行器名称',
  `address_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
INSERT INTO `xxl_job_group` VALUES (2, 'job-executor', '默认执行器', 1, 'http://127.0.0.1:9101/', '2022-07-04 10:00:12');

-- ----------------------------
-- Table structure for xxl_job_info
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_info`;
CREATE TABLE `xxl_job_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `author` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT 0 COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT 0 COMMENT '失败重试次数',
  `glue_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime NULL DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT 0 COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT 0 COMMENT '下次调度时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
INSERT INTO `xxl_job_info` VALUES (1, 1, 'productDataHandler', '2018-11-03 22:21:31', '2023-04-10 15:32:08', 'XXL', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'productDataHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '', 1, 1720169743041, 1720169863041);
INSERT INTO `xxl_job_info` VALUES (2, 1, 'allDayProductDataHandler', '2023-03-19 19:16:53', '2023-04-10 15:32:00', 'XXL', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'allDayProductDataHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-03-19 19:16:53', '', 1, 1720169743041, 1720169863041);
INSERT INTO `xxl_job_info` VALUES (3, 1, 'shutdownHandler', '2023-03-19 19:17:41', '2023-04-10 15:31:51', 'XXL', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'shutdownHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-03-19 19:17:41', '', 1, 1720169743041, 1720169863041);
INSERT INTO `xxl_job_info` VALUES (4, 1, 'thresholdHandler', '2023-03-19 19:18:31', '2023-03-20 09:56:43', 'XXL', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'thresholdHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-03-19 19:18:31', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (17, 2, '阈值报警', '2023-04-21 19:02:12', '2023-05-16 14:36:11', 'XXL', '', 'FIX_RATE', '5', 'DO_NOTHING', 'FIRST', 'thresholdHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-04-21 19:02:12', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (18, 2, '计算中间结果', '2023-05-05 09:59:25', '2023-05-05 09:59:25', 'XXL', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'productDataHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-05-05 09:59:25', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (19, 2, '产生中间计算结果(新)', '2023-05-05 14:13:49', '2023-05-08 11:28:18', 'XXL', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'transferDataHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-05-05 14:13:49', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (20, 2, '计算全天OEE', '2023-05-06 13:37:06', '2023-05-06 13:37:06', 'XXL', '', 'FIX_RATE', '120', 'DO_NOTHING', 'FIRST', 'allDayProductDataHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-05-06 13:37:06', '', 0, 0, 0);
INSERT INTO `xxl_job_info` VALUES (21, 2, '自动巡检(日)', '2023-05-22 10:36:29', '2023-09-18 10:48:52', 'XXL', '', 'CRON', '0 0 0 1/1 * ?', 'DO_NOTHING', 'FIRST', 'inspectionDayHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-05-22 10:36:29', '', 1, 1700755200000, 1720195200000);
INSERT INTO `xxl_job_info` VALUES (22, 2, '自动巡检(周)', '2023-05-22 10:38:46', '2023-09-18 10:48:49', 'XXL', '', 'CRON', '0 0 0 1/1 * ?', 'DO_NOTHING', 'FIRST', 'inspectionWeekHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-05-22 10:38:46', '', 1, 1700755200000, 1720195200000);
INSERT INTO `xxl_job_info` VALUES (23, 2, '自动巡检(月)', '2023-05-22 10:43:20', '2023-09-18 10:48:46', 'XXL', '', 'CRON', '0 0 0 1/1 * ?', 'DO_NOTHING', 'FIRST', 'inspectionMonthHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-05-22 10:43:20', '', 1, 1700755200000, 1720195200000);
INSERT INTO `xxl_job_info` VALUES (24, 2, '统计电能耗', '2023-08-25 10:54:12', '2023-08-25 10:56:06', 'XXL', '', 'FIX_RATE', '600', 'DO_NOTHING', 'FIRST', 'statisticsElectricityEnergy', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-08-25 10:54:12', '', 1, 1720169263043, 1720169863043);
INSERT INTO `xxl_job_info` VALUES (25, 2, '统计水能耗', '2023-08-25 13:45:40', '2023-08-25 13:46:34', 'XXL', '', 'FIX_RATE', '600', 'DO_NOTHING', 'FIRST', 'statisticsWaterEnergy', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-08-25 13:45:40', '', 1, 1720169263043, 1720169863043);
INSERT INTO `xxl_job_info` VALUES (26, 2, '统计电功率峰谷值平均值', '2023-09-08 14:15:55', '2023-10-18 10:49:54', 'XXL', '', 'FIX_RATE', '600', 'DO_NOTHING', 'FIRST', 'statisticsPower', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-09-08 14:15:55', '', 1, 1720169263043, 1720169863043);
INSERT INTO `xxl_job_info` VALUES (27, 2, '统计电流峰谷值平均值', '2023-10-18 10:49:40', '2023-10-18 10:51:18', 'XXL', '', 'FIX_RATE', '600', 'DO_NOTHING', 'FIRST', 'statisticsCurrent', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-10-18 10:49:40', '', 1, 1720169263043, 1720169863043);
INSERT INTO `xxl_job_info` VALUES (28, 2, '统计电压峰谷值平均值', '2023-10-18 10:50:54', '2023-10-18 10:51:15', 'XXL', '', 'FIX_RATE', '600', 'DO_NOTHING', 'FIRST', 'statisticsVoltage', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-10-18 10:50:54', '', 1, 1720169263043, 1720169863043);

-- ----------------------------
-- Table structure for xxl_job_lock
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_lock`;
CREATE TABLE `xxl_job_lock`  (
  `lock_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_lock
-- ----------------------------

-- ----------------------------
-- Table structure for xxl_job_log
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log`;
CREATE TABLE `xxl_job_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT 0 COMMENT '失败重试次数',
  `trigger_time` datetime NULL DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '调度-日志',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `I_trigger_time`(`trigger_time`) USING BTREE,
  INDEX `I_handle_code`(`handle_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3520353 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_log
-- ----------------------------
INSERT INTO `xxl_job_log` VALUES (3520321, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:39:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520322, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:39:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520323, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:39:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520324, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:41:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520325, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:41:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520326, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:41:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520327, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:43:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520328, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:43:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520329, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:43:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520330, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:45:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520331, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:45:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520332, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:45:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520333, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:47:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520334, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:47:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520335, 2, 24, 'http://127.0.0.1:9101/', 'statisticsElectricityEnergy', '', NULL, 0, '2024-07-05 16:47:43', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.106<br>执行器-注册方式：手动录入<br>执行器-地址列表：[http://127.0.0.1:9101/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://127.0.0.1:9101/<br>code：200<br>msg：null', '2024-07-05 16:47:44', 200, '', 0);
INSERT INTO `xxl_job_log` VALUES (3520336, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:47:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520337, 2, 25, 'http://127.0.0.1:9101/', 'statisticsWaterEnergy', '', NULL, 0, '2024-07-05 16:47:43', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.106<br>执行器-注册方式：手动录入<br>执行器-地址列表：[http://127.0.0.1:9101/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://127.0.0.1:9101/<br>code：200<br>msg：null', '2024-07-05 16:47:44', 200, '', 0);
INSERT INTO `xxl_job_log` VALUES (3520338, 2, 27, 'http://127.0.0.1:9101/', 'statisticsCurrent', '', NULL, 0, '2024-07-05 16:47:43', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.106<br>执行器-注册方式：手动录入<br>执行器-地址列表：[http://127.0.0.1:9101/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://127.0.0.1:9101/<br>code：200<br>msg：null', '2024-07-05 16:47:44', 200, '', 0);
INSERT INTO `xxl_job_log` VALUES (3520339, 2, 26, 'http://127.0.0.1:9101/', 'statisticsPower', '', NULL, 0, '2024-07-05 16:47:43', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.106<br>执行器-注册方式：手动录入<br>执行器-地址列表：[http://127.0.0.1:9101/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://127.0.0.1:9101/<br>code：200<br>msg：null', '2024-07-05 16:47:44', 200, '', 0);
INSERT INTO `xxl_job_log` VALUES (3520340, 2, 28, 'http://127.0.0.1:9101/', 'statisticsVoltage', '', NULL, 0, '2024-07-05 16:47:43', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.106<br>执行器-注册方式：手动录入<br>执行器-地址列表：[http://127.0.0.1:9101/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://127.0.0.1:9101/<br>code：200<br>msg：null', '2024-07-05 16:47:44', 200, '', 0);
INSERT INTO `xxl_job_log` VALUES (3520341, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:49:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520342, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:49:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520343, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:49:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520344, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:51:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520345, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:51:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520346, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:51:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520347, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:53:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520348, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:53:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520349, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:53:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520350, 1, 2, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:55:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520351, 1, 3, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:55:43', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `xxl_job_log` VALUES (3520352, 1, 1, NULL, NULL, NULL, NULL, 0, '2024-07-05 16:55:43', 0, NULL, NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for xxl_job_log_report
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log_report`;
CREATE TABLE `xxl_job_log_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime NULL DEFAULT NULL COMMENT '调度-时间',
  `running_count` int(11) NOT NULL DEFAULT 0 COMMENT '运行中-日志数量',
  `suc_count` int(11) NOT NULL DEFAULT 0 COMMENT '执行成功-日志数量',
  `fail_count` int(11) NOT NULL DEFAULT 0 COMMENT '执行失败-日志数量',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `i_trigger_day`(`trigger_day`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 277 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_log_report
-- ----------------------------
INSERT INTO `xxl_job_log_report` VALUES (119, '2022-07-04 00:00:00', 3, 0, 27, NULL);
INSERT INTO `xxl_job_log_report` VALUES (120, '2022-07-03 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (121, '2022-07-02 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (122, '2022-07-09 00:00:00', 0, 0, 29, NULL);
INSERT INTO `xxl_job_log_report` VALUES (123, '2022-07-08 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (124, '2022-07-07 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (125, '2022-07-11 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (126, '2022-07-10 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (127, '2022-07-12 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (128, '2022-07-14 00:00:00', 3, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (129, '2022-07-13 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (130, '2022-07-18 00:00:00', 0, 0, 2, NULL);
INSERT INTO `xxl_job_log_report` VALUES (131, '2022-07-17 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (132, '2022-07-16 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (133, '2022-07-20 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (134, '2022-07-19 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (135, '2022-07-29 00:00:00', 1, 0, 7, NULL);
INSERT INTO `xxl_job_log_report` VALUES (136, '2022-07-28 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (137, '2022-07-27 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (138, '2022-08-03 00:00:00', 1, 0, 47, NULL);
INSERT INTO `xxl_job_log_report` VALUES (139, '2022-08-02 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (140, '2022-08-01 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (141, '2022-08-14 00:00:00', 0, 0, 8, NULL);
INSERT INTO `xxl_job_log_report` VALUES (142, '2022-08-13 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (143, '2022-08-12 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (144, '2022-08-15 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (145, '2022-10-11 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (146, '2022-10-10 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (147, '2022-10-09 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (148, '2022-10-12 00:00:00', 0, 0, 5, NULL);
INSERT INTO `xxl_job_log_report` VALUES (149, '2022-10-15 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (150, '2022-10-14 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (151, '2022-10-13 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (152, '2022-10-18 00:00:00', 0, 1435, 5, NULL);
INSERT INTO `xxl_job_log_report` VALUES (153, '2022-10-17 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (154, '2022-10-16 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (155, '2022-11-03 00:00:00', 1, 0, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (156, '2022-11-02 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (157, '2022-11-01 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (158, '2023-03-14 00:00:00', 0, 0, 17, NULL);
INSERT INTO `xxl_job_log_report` VALUES (159, '2023-03-13 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (160, '2023-03-12 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (161, '2023-03-20 00:00:00', 0, 1, 116, NULL);
INSERT INTO `xxl_job_log_report` VALUES (162, '2023-03-19 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (163, '2023-03-18 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (164, '2023-04-11 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (165, '2023-04-10 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (166, '2023-04-09 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (167, '2023-04-18 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (168, '2023-04-17 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (169, '2023-04-16 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (170, '2023-04-21 00:00:00', 48, 0, 10, NULL);
INSERT INTO `xxl_job_log_report` VALUES (171, '2023-04-20 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (172, '2023-04-19 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (173, '2023-04-23 00:00:00', 72, 2, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (174, '2023-04-22 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (175, '2023-05-05 00:00:00', 261, 0, 9, NULL);
INSERT INTO `xxl_job_log_report` VALUES (176, '2023-05-04 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (177, '2023-05-03 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (178, '2023-05-06 00:00:00', 315, 0, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (179, '2023-05-08 00:00:00', 642, 0, 31, NULL);
INSERT INTO `xxl_job_log_report` VALUES (180, '2023-05-07 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (181, '2023-05-10 00:00:00', 675, 0, 1, NULL);
INSERT INTO `xxl_job_log_report` VALUES (182, '2023-05-09 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (183, '2023-05-16 00:00:00', 489, 0, 4, NULL);
INSERT INTO `xxl_job_log_report` VALUES (184, '2023-05-15 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (185, '2023-05-14 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (186, '2023-05-17 00:00:00', 681, 0, 2, NULL);
INSERT INTO `xxl_job_log_report` VALUES (187, '2023-05-22 00:00:00', 1218, 13, 1, NULL);
INSERT INTO `xxl_job_log_report` VALUES (188, '2023-05-21 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (189, '2023-05-20 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (190, '2023-05-23 00:00:00', 978, 0, 1, NULL);
INSERT INTO `xxl_job_log_report` VALUES (191, '2023-06-01 00:00:00', 58, 0, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (192, '2023-05-31 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (193, '2023-05-30 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (194, '2023-06-26 00:00:00', 384, 0, 8, NULL);
INSERT INTO `xxl_job_log_report` VALUES (195, '2023-06-25 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (196, '2023-06-24 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (197, '2023-06-30 00:00:00', 67, 0, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (198, '2023-06-29 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (199, '2023-06-28 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (200, '2023-07-04 00:00:00', 330, 0, 1, NULL);
INSERT INTO `xxl_job_log_report` VALUES (201, '2023-07-03 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (202, '2023-07-02 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (203, '2023-07-11 00:00:00', 132, 0, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (204, '2023-07-10 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (205, '2023-07-09 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (206, '2023-07-12 00:00:00', 4, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (207, '2023-08-25 00:00:00', 1116, 138, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (208, '2023-08-24 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (209, '2023-08-23 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (210, '2023-08-26 00:00:00', 2160, 288, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (211, '2023-08-27 00:00:00', 2160, 288, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (212, '2023-08-28 00:00:00', 2160, 288, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (213, '2023-08-29 00:00:00', 2160, 288, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (214, '2023-08-30 00:00:00', 975, 130, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (215, '2023-09-01 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (216, '2023-08-31 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (217, '2023-09-05 00:00:00', 942, 122, 2, NULL);
INSERT INTO `xxl_job_log_report` VALUES (218, '2023-09-04 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (219, '2023-09-03 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (220, '2023-09-06 00:00:00', 2160, 286, 2, NULL);
INSERT INTO `xxl_job_log_report` VALUES (221, '2023-09-07 00:00:00', 2145, 286, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (222, '2023-09-08 00:00:00', 2160, 342, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (223, '2023-09-09 00:00:00', 2160, 432, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (224, '2023-09-10 00:00:00', 2160, 432, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (225, '2023-09-11 00:00:00', 1539, 309, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (226, '2023-09-12 00:00:00', 1089, 216, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (227, '2023-09-13 00:00:00', 1869, 375, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (228, '2023-09-14 00:00:00', 792, 158, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (229, '2023-09-15 00:00:00', 402, 109, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (230, '2023-09-18 00:00:00', 795, 193, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (231, '2023-09-17 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (232, '2023-09-16 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (233, '2023-09-19 00:00:00', 78, 15, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (234, '2023-09-21 00:00:00', 159, 35, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (235, '2023-09-20 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (236, '2023-09-22 00:00:00', 360, 72, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (237, '2023-09-25 00:00:00', 741, 145, 3, NULL);
INSERT INTO `xxl_job_log_report` VALUES (238, '2023-09-24 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (239, '2023-09-23 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (240, '2023-09-26 00:00:00', 789, 156, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (241, '2023-09-27 00:00:00', 636, 126, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (242, '2023-10-16 00:00:00', 24, 4, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (243, '2023-10-15 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (244, '2023-10-14 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (245, '2023-10-17 00:00:00', 726, 145, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (246, '2023-10-18 00:00:00', 786, 236, 6, NULL);
INSERT INTO `xxl_job_log_report` VALUES (247, '2023-10-19 00:00:00', 52, 0, 11, NULL);
INSERT INTO `xxl_job_log_report` VALUES (248, '2023-10-25 00:00:00', 291, 99, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (249, '2023-10-24 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (250, '2023-10-23 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (251, '2023-10-26 00:00:00', 696, 242, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (252, '2023-10-27 00:00:00', 777, 255, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (253, '2023-10-30 00:00:00', 687, 226, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (254, '2023-10-29 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (255, '2023-10-28 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (256, '2023-10-31 00:00:00', 612, 195, 5, NULL);
INSERT INTO `xxl_job_log_report` VALUES (257, '2023-11-01 00:00:00', 225, 70, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (258, '2023-11-02 00:00:00', 714, 235, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (259, '2023-11-03 00:00:00', 669, 215, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (260, '2023-11-06 00:00:00', 708, 225, 10, NULL);
INSERT INTO `xxl_job_log_report` VALUES (261, '2023-11-05 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (262, '2023-11-04 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (263, '2023-11-07 00:00:00', 582, 190, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (264, '2023-11-08 00:00:00', 756, 250, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (265, '2023-11-09 00:00:00', 639, 200, 10, NULL);
INSERT INTO `xxl_job_log_report` VALUES (266, '2023-11-10 00:00:00', 783, 260, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (267, '2023-11-14 00:00:00', 255, 85, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (268, '2023-11-13 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (269, '2023-11-12 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (270, '2023-11-15 00:00:00', 192, 55, 5, NULL);
INSERT INTO `xxl_job_log_report` VALUES (271, '2023-11-23 00:00:00', 246, 84, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (272, '2023-11-22 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (273, '2023-11-21 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (274, '2024-07-05 00:00:00', 24, 5, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (275, '2024-07-04 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (276, '2024-07-03 00:00:00', 0, 0, 0, NULL);

-- ----------------------------
-- Table structure for xxl_job_logglue
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_logglue`;
CREATE TABLE `xxl_job_logglue`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_logglue
-- ----------------------------

-- ----------------------------
-- Table structure for xxl_job_registry
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_registry`;
CREATE TABLE `xxl_job_registry`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `registry_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `registry_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `i_g_k_v`(`registry_group`, `registry_key`, `registry_value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_registry
-- ----------------------------
INSERT INTO `xxl_job_registry` VALUES (132, 'EXECUTOR', 'xxl-job-executor', 'http://192.168.0.2:9101/', '2022-10-18 16:09:11');
INSERT INTO `xxl_job_registry` VALUES (134, 'EXECUTOR', 'xxl-job-executor', 'http://192.168.0.107:9101/', '2023-04-21 14:58:50');
INSERT INTO `xxl_job_registry` VALUES (135, 'EXECUTOR', 'xxl-job-executor', 'http://172.40.1.1:9101/', '2023-04-23 18:38:58');
INSERT INTO `xxl_job_registry` VALUES (136, 'EXECUTOR', 'xxl-job-executor', 'http://10.100.200.21:9101/', '2023-05-22 12:05:35');
INSERT INTO `xxl_job_registry` VALUES (138, 'EXECUTOR', 'xxl-job-executor', 'http://192.168.0.113:9101/', '2023-05-23 09:19:56');
INSERT INTO `xxl_job_registry` VALUES (151, 'EXECUTOR', 'xxl-job-executor', 'http://192.168.217.1:9101/', '2023-09-06 16:13:44');
INSERT INTO `xxl_job_registry` VALUES (166, 'EXECUTOR', 'xxl-job-executor', 'http://192.168.0.103:9101/', '2023-11-01 16:02:42');
INSERT INTO `xxl_job_registry` VALUES (183, 'EXECUTOR', 'xxl-job-executor', 'http://192.168.0.108:9101/', '2023-11-23 18:08:44');
INSERT INTO `xxl_job_registry` VALUES (184, 'EXECUTOR', 'xxl-job-executor', 'http://192.168.0.106:9101/', '2024-07-05 16:55:29');

-- ----------------------------
-- Table structure for xxl_job_user
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_user`;
CREATE TABLE `xxl_job_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `i_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
INSERT INTO `xxl_job_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
