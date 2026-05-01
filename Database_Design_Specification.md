# 监控类软件异常告警规则引擎 - 数据库设计文档

本设计方案旨在支撑“基于 SpringBoot + Netty”的后端架构与“高科技可视化大屏”的前端展示，实现从设备报文接入到规则判定、再到前端告警的全链路数据闭环。

## 0. 大屏接口 JSON 示例（前端数据契约）

> 说明：大屏前端（`web`）通过 `GET {baseUrl}/bigscreen/...` 拉取数据，响应体以 `success` 判断成功（见 `web/src/api/api.js`）。  
> 建议后端所有接口保持统一结构：`{ success: boolean, data: any, msg?: string }`。

### 0.1 `GET /bigscreen/countUserNum`（big1：用户总览）

**Response 示例**

```json
{
  "success": true,
  "data": {
    "totalNum": 218,
    "onlineNum": 120,
    "offlineNum": 90,
    "lockNum": 8
  },
  "msg": "ok"
}
```

### 0.2 `GET /bigscreen/countDeviceNum`（big2：设备总览）

> 当前大屏左上模块已调整为“平均温度/平均湿度/平均气压”，前端取值字段为：`temperature`、`humidity`、`pressure`。

**Response 示例**

```json
{
  "success": true,
  "data": {
    "temperature": 26.4,
    "humidity": 48,
    "pressure": 1012
  },
  "msg": "ok"
}
```

### 0.3 `GET /bigscreen/sbtx?limitNum=20`（big3：设备提醒）

**Request 示例**

```json
{
  "limitNum": 20
}
```

**Response 示例**

```json
{
  "success": true,
  "data": {
    "list": [
      {
        "gatewayno": 10000,
        "deviceId": "6c512d754bbcd6d7cd86abce0e0cac58",
        "onlineState": 1,
        "createTime": "2026-05-01 22:30:00",
        "provinceName": "甘肃省",
        "cityName": "敦煌市",
        "countyName": "敦煌市"
      }
    ]
  },
  "msg": "ok"
}
```

### 0.4 `GET /bigscreen/alarmNum`（big4：报警次数）

**Response 示例**

```json
{
  "success": true,
  "data": {
    "dateList": ["2026-01", "2026-02", "2026-03", "2026-04", "2026-05", "2026-06"],
    "numList": [120, 98, 140, 60, 88, 30],
    "numList2": [80, 120, 70, 90, 40, 110]
  },
  "msg": "ok"
}
```

### 0.5 `GET /bigscreen/ssyj?limitNum=50`（big5：实时预警）

**Request 示例**

```json
{
  "limitNum": 50
}
```

**Response 示例**

```json
{
  "success": true,
  "data": {
    "list": [
      {
        "gatewayno": 10000,
        "terminalno": 100,
        "alertname": "水浸告警",
        "alertvalue": 126.5,
        "alertdetail": "传感器读数异常，请检查线路与供电",
        "phase": "A1",
        "sbInfo": "设备运行信息摘要（示例）",
        "createtime": "2026-05-01 22:30:00",
        "provinceName": "甘肃省",
        "cityName": "敦煌市",
        "countyName": "敦煌市",
        "deviceid": null
      }
    ]
  },
  "msg": "ok"
}
```

### 0.6 `GET /bigscreen/topology?stationNo=DH-01`（big6：拓扑图数据）

> 说明：big6 现改为“拓扑图”数据接口，用于驱动大屏“安装计划”区域替换后的拓扑展示。

**Request 示例**

```json
{
  "stationNo": "DH-01"
}
```

**Response 示例**

```json
{
  "success": true,
  "data": {
    "nodes": [
      { "id": "site:DH-01", "name": "敦煌站", "type": "SITE", "stationNo": "DH-01", "status": "ONLINE" },
      { "id": "dev:SW-1", "name": "交换机", "type": "SWITCH", "stationNo": "DH-01", "deviceNo": "SW-1", "status": "ONLINE" },
      { "id": "dev:SRV-1", "name": "授时服务器", "type": "SERVER", "stationNo": "DH-01", "deviceNo": "SRV-1", "status": "ONLINE" },
      { "id": "dev:PHASE-1", "name": "相位微调器", "type": "PHASE_TUNER", "stationNo": "DH-01", "deviceNo": "PHASE-1", "status": "ONLINE" },
      { "id": "dev:CLK-M", "name": "氢原子钟(主)", "type": "CLOCK_MAIN", "stationNo": "DH-01", "deviceNo": "CLK-M", "status": "ONLINE" },
      { "id": "dev:CLK-B", "name": "氢原子钟(备)", "type": "CLOCK_BACKUP", "stationNo": "DH-01", "deviceNo": "CLK-B", "status": "OFFLINE" },
      { "id": "dev:GNSS-1", "name": "GNSS天线", "type": "GNSS", "stationNo": "DH-01", "deviceNo": "GNSS-1", "status": "ONLINE" }
    ],
    "links": [
      { "source": "site:DH-01", "target": "dev:SW-1", "linkType": "PHYSICAL", "label": "网络" },
      { "source": "dev:SW-1", "target": "dev:SRV-1", "linkType": "PHYSICAL", "label": "以太网" },
      { "source": "dev:SW-1", "target": "dev:PHASE-1", "linkType": "LOGICAL", "label": "控制" },
      { "source": "dev:PHASE-1", "target": "dev:CLK-M", "linkType": "REFERENCE", "label": "参考" },
      { "source": "dev:PHASE-1", "target": "dev:CLK-B", "linkType": "REFERENCE", "label": "参考" },
      { "source": "dev:GNSS-1", "target": "dev:SRV-1", "linkType": "LOGICAL", "label": "授时" }
    ]
  },
  "msg": "ok"
}
```

### 0.7 `GET /bigscreen/ranking`（big7：报警排名 TOP8）

> 该接口在前端用于 `dv-capsule-chart`，数据结构为数组：每项包含 `name` 与 `value`。

**Response 示例**

```json
{
  "success": true,
  "data": [
    { "name": "敦煌市", "value": 820 },
    { "name": "酒泉市", "value": 610 },
    { "name": "嘉峪关市", "value": 490 }
  ],
  "msg": "ok"
}
```

### 0.8 `GET /bigscreen/centermap?regionCode=china`（big8：中间地图）

**Request 示例**

```json
{
  "regionCode": "china"
}
```

**Response 示例（regionCode=china）**

```json
{
  "success": true,
  "data": {
    "regionCode": "china",
    "dataList": [
      { "name": "甘肃省", "value": 320 },
      { "name": "青海省", "value": 180 }
    ]
  },
  "msg": "ok"
}
```

**Response 示例（regionCode=某省/市编码，下钻到城市）**

```json
{
  "success": true,
  "data": {
    "regionCode": "620000",
    "dataList": [
      { "name": "敦煌市", "value": 120 },
      { "name": "酒泉市", "value": 200 }
    ]
  },
  "msg": "ok"
}
```

### 0.9 （可选保留）`GET /bigscreen/installationPlan`（历史接口：安装计划）

> 说明：如不再使用“安装计划”统计图，可不实现此接口；若需要兼容旧前端/回滚，可保留。

**Response 示例**

```json
{
  "success": true,
  "data": {
    "category": ["敦煌市", "酒泉市", "嘉峪关市"],
    "barData": [20, 15, 30],
    "lineData": [30, 22, 40],
    "rateData": ["67", "68", "75"]
  },
  "msg": "ok"
}
```

## 1. 数据库表结构（重设计：以 big1~big8/big6 拓扑为准）

> 目标：保证大屏所有接口（big1~big8，big6=拓扑）都能从数据库中**稳定取数**，避免“字段缺失/口径不一致/地域维度对不上/拓扑无数据”。

### 1.1 站点信息表 `station_info`

用于：big8 地图聚合/下钻、big3/big5 省市区展示、拓扑按站点查询。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| station_no | varchar(50) | 站点编号（如 DH-01） | Unique, Not Null |
| station_name | varchar(100) | 站点名称（如 敦煌站） | Not Null |
| region_code | varchar(20) | 行政区划编码（用于 big8 regionCode 下钻/聚合） | Index |
| region_name | varchar(50) | 与 GeoJSON `properties.name` 匹配的名称（省/市名） | - |
| province | varchar(50) | 省 | - |
| city | varchar(50) | 市 | - |
| county | varchar(50) | 区/县 | - |
| longitude | decimal(10,6) | 经度 | - |
| latitude | decimal(10,6) | 纬度 | - |
| location_desc | varchar(255) | 详细位置描述 | - |
| is_enabled | tinyint | 是否启用（0/1） | Default 1 |
| created_at | datetime | 创建时间 | - |
| updated_at | datetime | 更新时间 | - |

索引建议：
- `idx_station_region_code(region_code)`
- `idx_station_station_no(station_no)`

### 1.2 设备信息表 `device_info`

用于：device_no 与站点/类型/展示名的映射，告警/预警/拓扑的关联。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| device_no | varchar(50) | 设备唯一编号（报文识别码） | Unique, Not Null |
| gatewayno | varchar(50) | 前端展示“设备ID/gatewayno”（若你们口径就是 device_no，可与 device_no 保持一致） | Index |
| device_name | varchar(100) | 设备名称（如 氢原子钟） | Not Null |
| device_type | varchar(50) | 设备型号/类型（可映射 terminalno） | - |
| terminalno | varchar(50) | 前端展示“型号/terminalno”（可选冗余，避免接口层做映射） | - |
| station_id | bigint | 所属站点ID | FK -> station_info.id, Index |
| owner | varchar(50) | 负责人 | - |
| install_date | datetime | 安装日期 | - |
| is_enabled | tinyint | 是否启用（0/1） | Default 1 |
| created_at | datetime | 创建时间 | - |
| updated_at | datetime | 更新时间 | - |

索引建议：
- `idx_device_station_id(station_id)`
- `idx_device_gatewayno(gatewayno)`

### 1.3 设备运行状态表 `device_runtime_status`

用于：big2 温湿压（实时/平均的基础数据）、拓扑节点状态、在线/离线判定。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| device_no | varchar(50) | 设备编号 | PK, FK -> device_info.device_no |
| current_temp | double | 当前温度 | - |
| current_humi | double | 当前湿度 | - |
| current_pres | double | 当前气压 | - |
| run_status | int | 运行状态（1-在线，2-离线，3-维护/停用等） | Default 2 |
| last_update_time | datetime | 最后一次上报时间 | Index |

索引建议：
- `idx_runtime_last_update(last_update_time)`
- `idx_runtime_status(run_status)`

### 1.4 设备事件流水表 `device_event_log`（新增：支撑 big3）

用于：big3 设备提醒（上线/下线/状态变化）的“列表流水”。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| device_no | varchar(50) | 设备编号 | Not Null, Index |
| station_id | bigint | 站点ID | Index |
| event_type | varchar(20) | 事件类型（ONLINE/OFFLINE/ALARM/RECOVER/STATUS_CHANGE） | Not Null |
| online_state | tinyint | 兼容前端字段（0-下线，1-上线） | - |
| event_time | datetime | 事件时间（用于 createTime） | Index |
| ext_json | text | 扩展字段（报文摘要/原因等） | - |

### 1.5 告警规则表 `rules_table`

用于：规则启用、级别、连续次数等。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| rule_id | bigint | 规则ID | PK, Auto Inc |
| rule_name | varchar(100) | 规则名称 | Not Null |
| scene_type | int | 场景类型（1-阈值, 2-工况, 3-超时） | Not Null |
| is_enabled | tinyint | 是否启用（0/1） | Default 1 |
| alarm_level | int | 告警级别（1/2/3） | Default 1 |
| accumulate_count | int | 连续异常 N 次才报警 | Default 1 |
| created_at | datetime | 创建时间 | - |
| updated_at | datetime | 更新时间 | - |

### 1.6 阈值规则明细表 `threshold_rule_table`

用于：阈值规则具体指标范围。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| rule_id | bigint | 关联规则ID | FK -> rules_table.rule_id, Index |
| resource_type | varchar(20) | 监控指标（Temperature/Humidity/Pressure） | Not Null |
| min_value | double | 正常范围下限 | - |
| max_value | double | 正常范围上限 | - |

### 1.7 告警日志表 `rules_alarm_log`（增强：支撑 big4/big5/big7）

用于：
- big5 实时预警列表（需要告警名称/值/详情/相位/型号等）
- big4 报警次数趋势（按时间聚合，且可按级别/类型拆两条线）
- big7 报警排名 TOP8（按站点/城市聚合）

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 告警ID | PK, Auto Inc |
| device_no | varchar(50) | 报警设备编号 | Not Null, Index |
| station_id | bigint | 站点ID（写入时冗余，避免每次 JOIN） | Index |
| rule_id | bigint | 触发规则ID | Index |
| alarm_level | int | 告警级别（用于 big4 分线） | - |
| alarm_type | varchar(50) | 告警类型/分类（用于 big4 分线） | - |
| alarm_name | varchar(100) | 告警名称（alertname） | - |
| alarm_value | double | 告警值（alertvalue） | - |
| alarm_detail | varchar(500) | 告警详情（alertdetail） | - |
| phase | varchar(20) | 相位（phase） | - |
| sb_info | varchar(500) | 设备运行信息摘要（sbInfo） | - |
| alarm_time | datetime | 告警时间（createtime） | Index |
| is_handled | tinyint | 是否已处理（0/1） | Default 0 |

索引建议：
- `idx_alarm_time(alarm_time)`
- `idx_alarm_station_time(station_id, alarm_time)`
- `idx_alarm_device_time(device_no, alarm_time)`

### 1.8 规则异常累计计数表 `alarm_count_table`

用于：连续次数逻辑（过滤抖动）。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| device_no | varchar(50) | 设备编号 | Index |
| rule_id | bigint | 规则ID | Index |
| current_count | int | 当前连续异常次数 | - |
| last_check_time | datetime | 最后检查时间 | - |

唯一约束建议：
- `uniq_device_rule(device_no, rule_id)`

### 1.9 拓扑节点表 `topology_node`（新增：支撑 big6）

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| station_id | bigint | 站点ID | Not Null, Index |
| node_id | varchar(80) | 节点业务ID（用于返回 nodes[].id，如 dev:CLK-M） | Unique, Not Null |
| node_name | varchar(100) | 节点名称 | Not Null |
| node_type | varchar(50) | 节点类型（SITE/SWITCH/SERVER/CLOCK_MAIN/...） | Not Null |
| device_no | varchar(50) | 绑定设备（可为空，表示逻辑节点） | Index |
| sort_no | int | 排序 | Default 0 |
| ext_json | text | 扩展（图标/坐标/端口等） | - |

### 1.10 拓扑连边表 `topology_link`（新增：支撑 big6）

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| station_id | bigint | 站点ID | Not Null, Index |
| source_node_id | varchar(80) | 源节点 node_id | Not Null, Index |
| target_node_id | varchar(80) | 目标节点 node_id | Not Null, Index |
| link_type | varchar(30) | 边类型（PHYSICAL/LOGICAL/REFERENCE/CONTROL） | Not Null |
| label | varchar(50) | 边展示文本（label） | - |
| is_enabled | tinyint | 是否启用 | Default 1 |
| ext_json | text | 扩展（端口/方向/权重等） | - |

### 1.11 大屏指标快照表 `bigscreen_metric_snapshot`（新增：支撑 big2 平均值与高频刷新）

> 可选但推荐：如果大屏刷新频繁，建议每 N 秒/分钟产出快照，接口直接读快照，避免实时聚合压力。

| 字段名 | 类型 | 说明 | 约束 |
| :--- | :--- | :--- | :--- |
| id | bigint | 主键ID | PK, Auto Inc |
| scope_type | varchar(20) | 统计范围（GLOBAL/STATION） | Not Null |
| scope_key | varchar(50) | 范围标识（GLOBAL=ALL，STATION=station_no） | Index |
| temperature | double | 平均温度 | - |
| humidity | double | 平均湿度 | - |
| pressure | double | 平均气压 | - |
| snapshot_time | datetime | 快照时间 | Index |

---

## 2. 接口到表的映射（实现提示）

- **big2（温湿压）**：优先读取 `bigscreen_metric_snapshot` 最近一条；无快照则聚合 `device_runtime_status`（可按站点过滤）。
- **big3（设备提醒）**：从 `device_event_log` 按 `event_time desc` 取 `limitNum` 条；地域信息可由 `station_info` 拼装。
- **big4（报警次数）**：从 `rules_alarm_log` 按时间聚合；`numList/numList2` 建议按 `alarm_level` 或 `alarm_type` 区分。
- **big5（实时预警）**：从 `rules_alarm_log` 按 `alarm_time desc` 取 N 条，并 JOIN `device_info/station_info` 补齐地域与型号字段。
- **big6（拓扑）**：`topology_node/topology_link` 按 `stationNo -> station_id` 查询，节点状态从 `device_runtime_status` 推导。
- **big7（报警排名）**：`rules_alarm_log` 按 `station_id` 或城市聚合，Top8 返回 `name/value`。
- **big8（地图）**：`station_info.region_code/region_name` 用于聚合与名称匹配 GeoJSON。

