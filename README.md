# Wain（基础平台 / 管理系统）

这是一个 **Spring Boot（Java 8）多模块后端** + **Vue2 前端** 的管理平台项目，仓库中包含：

- **后端**：Maven 聚合工程，应用入口在 `platform-admin`
- **管理端前端**：`platform-ui`（Vue2 + Element UI）
- **大屏/展示端前端**：`web`（基于 `vue-big-screen` 二次修改）

## 功能概览

- **基础能力**：登录鉴权、用户/角色/菜单/部门、字典、参数配置、通知公告等
- **监控与运维**：在线用户、操作日志、登录日志、服务监控、缓存监控等
- **扩展能力**：定时任务（`platform-quartz`）、代码生成（`platform-generator`）、报表（Jimureport）等

> 具体业务功能以 `platform-business`、`platform-system` 中的模块为准。

## 技术栈

- **后端**：Spring Boot 2.5.x、MyBatis-Plus、Druid、Redis、Swagger/Knife4j
- **前端（管理端）**：Vue2、Vue CLI 4.x、Element UI、Axios
- **前端（大屏）**：Vue2、DataV、ECharts、Axios（可选 Mock）

## 环境要求

- **JDK**：8
- **Maven**：3.6+（建议）
- **Node.js / npm**：
  - `platform-ui` 的 `package.json` 声明 `node >= 8.9`
  - `web` 基于 Vue CLI 4.x，建议 Node 12+（若使用旧版 Node，请以本机实际可构建为准）
- **依赖服务**：MySQL、Redis（按当前 `application-*.yml` 配置）

## 快速开始

### 1）后端启动（本地）

默认使用 `dev` 配置（见 `platform-admin/src/main/resources/application.yml`）。

- **方式 A：IDE 运行**
  - 运行启动类 `com.smallway.PlatformApplication`

- **方式 B：命令行运行**

```bash
# 在仓库根目录
mvn -pl platform-admin -am spring-boot:run
```

启动成功后：

- **后端端口**：`8089`（见 `application-dev.yml` / `application-fat.yml` 的 `server.port`）
- **接口前缀**：通常为 `/dev-api`（Swagger 配置 `swagger.pathMapping`）
- **接口文档**（不同版本可能略有差异）：
  - `/swagger-ui/`
  - `/doc.html`（Knife4j）

### 2）管理端前端（`platform-ui`）

```bash
cd platform-ui
npm install

# 可选：国内镜像
# npm install --registry=https://registry.npmmirror.com

npm run dev
```

### 3）大屏/展示端（`web`）

```bash
cd web
npm install
npm start
```

提示：

- 大屏页面建议 **F11 全屏**。
- 如需关闭 Mock，请按 `web/README.md` 中说明在 `src/main.js` 注释 `require('./mock/mock')`。

## 配置说明

- **Profile 切换**：
  - `platform-admin/src/main/resources/application.yml` 默认 `spring.profiles.active: dev`
  - 可通过启动参数覆盖：`-Dspring.profiles.active=fat`（示例）
- **数据库与 Redis**：
  - 数据源/Redis 配置位于 `platform-admin/src/main/resources/application-*.yml`
  - 建议在本地使用自己的 MySQL/Redis，并避免把真实账号密码提交到仓库历史中
- **文件上传路径**：`ruoyi.profile`

## 目录结构（后端）

- `platform-admin`：Web 服务入口（启动模块）
- `platform-framework`：框架核心（鉴权、拦截器、通用配置等）
- `platform-system`：系统模块（用户/权限等）
- `platform-business`：业务模块（项目定制业务）
- `platform-common`：通用工具与基础能力
- `platform-quartz`：定时任务
- `platform-generator`：代码生成

## 常见问题

- **端口冲突**：修改 `application-*.yml` 的 `server.port`
- **接口 404/跨域**：确认前端代理配置与后端 `swagger.pathMapping` / 实际网关前缀一致
- **数据库连不上**：检查 `application-*.yml` 的 JDBC 地址、账号密码、网络可达性与时区参数

## 子项目说明

- `platform-ui/README.md`：管理端前端开发/构建说明
- `web/README.md`：大屏项目说明（来源、组件、Mock、适配等）

---

如果你希望我进一步把 README 写得更贴合你们的实际使用（例如：明确数据库初始化脚本位置、默认账号、前端代理到后端的具体地址、打包部署到 Nginx 的示例配置），我可以继续把这些信息从仓库里补齐并更新此文档。

