-- 监控告警规则引擎（论文业务）菜单与按钮权限
-- 执行前请确认 menu_id 与现有库不冲突；若冲突请整体改 21000 起算
-- 执行后：系统管理 -> 角色管理 -> 管理员 -> 勾选新菜单并保存

SET NAMES utf8mb4;

INSERT INTO `sys_menu` VALUES (21000, '监控告警规则引擎', 0, 15, 'alertRuleEngine', NULL, '', 1, 0, 'M', '0', '0', '', 'eye-open', 'admin', NOW(), '', NULL, '毕业设计：异常告警规则引擎业务');

INSERT INTO `sys_menu` VALUES (21001, '监控设备信息', 21000, 1, 'monDevice', '', '', 1, 0, 'C', '0', '0', 'monitor:device:list', 'build', 'admin', NOW(), '', NULL, '论文：设备管理模块');
INSERT INTO `sys_menu` VALUES (21002, '设备查询', 21001, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:device:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21003, '设备新增', 21001, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:device:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21004, '设备修改', 21001, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:device:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21005, '设备删除', 21001, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:device:remove', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` VALUES (21010, '规则总表', 21000, 2, 'monRule', '', '', 1, 0, 'C', '0', '0', 'monitor:rule:list', 'documentation', 'admin', NOW(), '', NULL, '论文表4.1');
INSERT INTO `sys_menu` VALUES (21011, '规则查询', 21010, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:rule:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21012, '规则新增', 21010, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:rule:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21013, '规则修改', 21010, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:rule:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21014, '规则删除', 21010, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:rule:remove', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` VALUES (21020, '报警次数累计表', 21000, 3, 'monRuleCount', '', '', 1, 0, 'C', '0', '0', 'monitor:ruleCount:list', 'chart', 'admin', NOW(), '', NULL, '论文表4.2');
INSERT INTO `sys_menu` VALUES (21021, '累计查询', 21020, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:ruleCount:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21022, '累计新增', 21020, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:ruleCount:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21023, '累计修改', 21020, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:ruleCount:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21024, '累计删除', 21020, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:ruleCount:remove', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` VALUES (21030, '阈值范围场景表', 21000, 4, 'monRuleThreshold', '', '', 1, 0, 'C', '0', '0', 'monitor:threshold:list', 'slider', 'admin', NOW(), '', NULL, '论文表4.3');
INSERT INTO `sys_menu` VALUES (21031, '阈值查询', 21030, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:threshold:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21032, '阈值新增', 21030, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:threshold:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21033, '阈值修改', 21030, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:threshold:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21034, '阈值删除', 21030, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:threshold:remove', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` VALUES (21040, '工况状态报警表', 21000, 5, 'monRuleWorkstate', '', '', 1, 0, 'C', '0', '0', 'monitor:workstate:list', 'component', 'admin', NOW(), '', NULL, '论文表4.4');
INSERT INTO `sys_menu` VALUES (21041, '工况查询', 21040, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:workstate:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21042, '工况新增', 21040, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:workstate:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21043, '工况修改', 21040, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:workstate:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21044, '工况删除', 21040, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:workstate:remove', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` VALUES (21050, '通讯超时场景表', 21000, 6, 'monRuleCommTimeout', '', '', 1, 0, 'C', '0', '0', 'monitor:commTimeout:list', 'link', 'admin', NOW(), '', NULL, '论文表4.5');
INSERT INTO `sys_menu` VALUES (21051, '超时查询', 21050, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:commTimeout:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21052, '超时新增', 21050, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:commTimeout:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21053, '超时修改', 21050, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:commTimeout:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21054, '超时删除', 21050, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:commTimeout:remove', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO `sys_menu` VALUES (21060, '告警记录表', 21000, 7, 'monAlarmRecord', '', '', 1, 0, 'C', '0', '0', 'monitor:alarm:list', 'message', 'admin', NOW(), '', NULL, '报文持久化');
INSERT INTO `sys_menu` VALUES (21061, '告警查询', 21060, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:alarm:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21062, '告警新增', 21060, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:alarm:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21063, '告警处理', 21060, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:alarm:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (21064, '告警删除', 21060, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:alarm:remove', '#', 'admin', NOW(), '', NULL, '');

-- 赋予超级管理员角色（role_id=1）以上菜单
INSERT INTO `sys_role_menu` SELECT 1, menu_id FROM `sys_menu` WHERE menu_id BETWEEN 21000 AND 21064;
