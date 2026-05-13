# Wain

Wain 是一套 **监控类软件异常告警规则引擎** 配套的基础平台：包含 **Spring Boot 多模块后端**、**管理端 Vue2（platform-ui）** 与 **可视化大屏 Vue2（web）**。后端提供大屏数据接口（`/bigscreen/*`），接口与数据库契约说明见仓库根目录 **`Database_Design_Specification.md`**。

## 技术栈与坐标

| 层级 | 说明 |
|------|------|
| **Maven** | 聚合工程 `top.smallway:platform:3.8.7` |
| **后端 Java 包** | `com.smallway`（启动类 `com.smallway.PlatformApplication`） |
| **后端** | Spring Boot 2.5.x、MyBatis-Plus、Druid、Redis、Swagger / Knife4j、积木报表（Jimureport）等 |
| **管理端 platform-ui** | Vue 2、Vue CLI 4、Element UI、Axios（默认标题「异常告警规则引擎」） |
| **大屏 web** | Vue 2、DataV、ECharts、Axios（可选 Mock） |

## 仓库结构

```
Wain/
├── pom.xml                      # Maven 父工程（groupId: top.smallway）
├── platform-admin/              # Web 入口、启动类、application-*.yml
├── platform-framework/          # 安全、AOP、Web 扩展等
├── platform-system/             # 系统管理（用户、角色、菜单等）
├── platform-common/             # 通用工具与公共模型
├── platform-business/           # 业务与大屏 API（如 BigscreenController）
├── platform-quartz/             # 定时任务
├── platform-generator/          # 代码生成
├── platform-ui/                 # 管理端前端
├── web/                         # 大屏前端
└── Database_Design_Specification.md  # 大屏接口 JSON 示例与表结构设计
```

## 环境要求

- **JDK 8**
- **Maven 3.6+**（本机需已配置 `mvn` 命令）
- **MySQL**、**Redis**（以 `platform-admin/src/main/resources/application-*.yml` 为准）
- **Node.js**：管理端与大屏建议使用 **Node 12+**（`platform-ui` 的 `engines` 为 `node >= 8.9`，实际构建以本机为准）

## 后端启动

1. 修改 `platform-admin/src/main/resources/application-dev.yml`（或对应 Profile）中的 **数据源、Redis、上传目录** 等，使之指向你的本地环境。
2. 确认 MyBatis 类型别名包与当前包名一致（已配置为 `com.smallway.**.domain`），否则 Mapper XML 中的短别名会解析失败。

**IDE**：运行 `com.smallway.PlatformApplication`。

**命令行**（在仓库根目录）：

```bash
mvn -pl platform-admin -am spring-boot:run
```

- 默认 Profile：`spring.profiles.active` 在 `application.yml` 中一般为 `dev`。
- 开发环境 HTTP 端口以 `application-dev.yml` 中 `server.port` 为准（常见为 **8089**）。
- 接口文档：Swagger / Knife4j（路径以实际工程配置为准，如 `/swagger-ui/`、`/doc.html`）。

### 大屏 REST 接口（节选）

控制器：`platform-business/.../BigscreenController.java`，前缀 **`/bigscreen`**。

| 路径 | 说明 |
|------|------|
| `GET /bigscreen/countUserNum` | big1 用户总览 |
| `GET /bigscreen/countDeviceNum` | big2 温湿压等指标 |
| `GET /bigscreen/sbtx` | big3 设备提醒 |
| `GET /bigscreen/alarmNum` | big4 报警次数趋势 |
| `GET /bigscreen/ssyj` | big5 实时预警 |
| `GET /bigscreen/topology` | big6 拓扑图（参数 `stationNo` 可选） |
| `GET /bigscreen/ranking` | big7 报警排名 |
| `GET /bigscreen/centermap` | big8 中间地图 |
| `GET /bigscreen/installationPlan` | 兼容旧版安装计划图表 |

请求/响应 JSON 示例见 **`Database_Design_Specification.md`** 第 0 章。

## 管理端前端（platform-ui）

```bash
cd platform-ui
npm install
# 可选：npm install --registry=https://registry.npmmirror.com
npm run dev
```

- 开发服务端口见 `platform-ui/vue.config.js`（默认 **8080**）。
- 开发环境接口基地址见 `platform-ui/.env.development`（当前示例为直连 **`http://localhost:8089`**）；生产/预发见 `.env.production`、`.env.staging` 中的 `VUE_APP_BASE_API`。
- 运行时前端拉取的后端根地址也可由 `platform-ui/public/config.js` 中的 `src` 配置。
- 首页若使用 **iframe 嵌入大屏**，请避免与管理端、大屏 **同一端口** 同时占用（例如管理端 8080、大屏改用其它端口或修改 `web` 的 devServer 端口）。

## 大屏前端（web）

```bash
cd web
npm install
npm start
```

- 建议大屏全屏展示（如 **F11**）。
- Mock 开关见 `web/README.md` 与 `web/src/main.js`。

## 配置与注意事项

- **Profile**：`platform-admin/src/main/resources/application.yml` 中 `spring.profiles.active`。
- **文件上传路径**：配置项 `ruoyi.profile`（各环境 yml 中）。
- **跨域 / 代理**：`platform-ui` 开发代理在 `vue.config.js` 中指向后端；若 `.env.development` 使用完整 URL，则请求不经过该 `proxy` 配置，需保证浏览器可访问后端地址。
- **iframe 嵌套**：若大屏站点返回 `X-Frame-Options` 等禁止嵌入的响应头，需在对应前端或网关侧放行。

## 常见问题

- **Maven 找不到**：在终端执行 `mvn -v`，确保已安装并配置 PATH。
- **MyBatis 别名 `SysConfig` 等解析失败**：检查 `application-*.yml` 中 `mybatis-plus.typeAliasesPackage` 是否包含 `com.smallway.**.domain`。
- **端口冲突**：修改 `server.port` 或各前端 `vue.config.js` / `package.json` 中的 dev 端口。

## 更多文档

- `platform-ui/README.md`：管理端构建与脚本说明  
- `web/README.md`：大屏项目说明与 Mock  
- `Database_Design_Specification.md`：大屏接口契约与数据库表设计  
