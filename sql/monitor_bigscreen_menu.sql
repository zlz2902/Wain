-- 作者：Smallway
-- 日期：2026-05-13
-- 描述：大屏图表权限菜单SQL，用于控制不同角色对大屏图表的访问权限
-- 确保 menu_id 22000-22008 在你的 sys_menu 表中不冲突

SET NAMES utf8mb4;

-- 插入大屏权限配置的虚拟目录（visible设为1，表示隐藏该菜单，仅用于权限配置）
INSERT INTO `sys_menu` VALUES (22000, '大屏图表权限', 0, 16, 'bigscreenPerms', NULL, '', 1, 0, 'M', '1', '0', '', 'chart', 'admin', NOW(), '', NULL, '大屏各模块显隐权限配置');

-- 插入各个图表的按钮权限
INSERT INTO `sys_menu` VALUES (22001, '设备信息总览', 22000, 1, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:user-overview', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (22002, '设备状态总览', 22000, 2, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:device-overview', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (22003, '设备提醒', 22000, 3, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:device-tips', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (22004, '中间地图', 22000, 4, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:map', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (22005, '安装计划', 22000, 5, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:topology', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (22006, '报警次数', 22000, 6, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:alarm-count', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (22007, '报警排名', 22000, 7, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:alarm-ranking', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (22008, '数据统计图', 22000, 8, '', '', '', 1, 0, 'F', '0', '0', 'dashboard:chart:realtime-warning', '#', 'admin', NOW(), '', NULL, '');

-- 默认给 admin 角色（role_id=1）赋予大屏所有图表权限
INSERT INTO `sys_role_menu` SELECT 1, menu_id FROM `sys_menu` WHERE menu_id BETWEEN 22000 AND 22008;