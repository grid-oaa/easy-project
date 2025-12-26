# agents.md（项目级 AI/Agent 指南）
> 本文档用于帮助 AI Agent（如 Codex、Copilot、ChatGPT）快速理解并安全地在本仓库中工作。
> 内容包含：项目目录结构、代码风格与命名规范、构建/启动命令、主要依赖组件（后端/前端）、以及配置文件关键项说明。
> 要求：补全文档时严格保持章节顺序与标题不变。

---

## 1. 项目概览

- 项目名称：easy-backend-framework（父 POM `pom.xml`），仓库目录：easy-project
- 项目类型：后端多模块服务（Spring Boot/Spring Cloud），未发现前端工程（无 `package.json`）
- 核心职责与主要功能：API 网关鉴权、认证登录与 JWT、权限/RBAC 校验、基础监控与指标暴露
- 高层架构概述：Gateway 作为统一入口 -> auth-service/permission-service；Nacos 负责服务发现（配置中心已禁用）；MySQL 存业务数据；Redis 存刷新令牌与访问令牌黑名单；Prometheus/Grafana/Sentinel 用于监控/治理
- 技术栈总览：
    - 后端：Java 17、Spring Boot 3.2.5、Spring Cloud 2023.0.2、Spring Cloud Alibaba 2023.0.1.0、MyBatis-Plus 3.5.6、JJWT 0.12.5
    - 前端：未发现（仓库内无前端构建文件）
    - 基础设施/中间件：Docker Compose、Nacos 2.3.0、MySQL 8.0、Redis 7.2、Prometheus 2.51.2、Grafana 10.4.1、Sentinel Dashboard 1.8.7

TODO(need-confirmation)：
- 是否存在独立前端仓库或前端子模块

---

## 2. 项目目录结构

### 2.1 顶层目录结构（Top-Level）
> 使用 tree 形式展示顶层目录，并给出职责说明。
```text
.
├── .git/                     # Git 元数据
├── .idea/                    # IntelliJ 项目配置
├── auth-service/             # 认证服务（注册/登录/JWT/刷新）
├── common/                   # 公共响应与错误码
├── docker/                   # 基础设施配置（Prometheus）
├── gateway-service/          # API 网关（鉴权/路由/限流接入）
├── permission-service/       # 权限服务（RBAC/权限校验）
├── .env                      # 环境变量模板
├── docker-compose.yml        # 基础服务编排（Nacos/MySQL/Redis/监控等）
├── pom.xml                   # 父 POM（版本/依赖管理/模块聚合）
├── 运行说明.md               # 启动与运行说明
├── 虚拟机前置服务安装.md     # VM 依赖安装指南
├── 设计文档.md               # 架构/设计说明
├── 需求文档.md               # 需求说明
└── 开发任务文档.md           # 任务与交付说明
```

### 2.2 后端目录结构（如存在）
- 模块划分方式：Maven 多模块（`common`、`gateway-service`、`auth-service`、`permission-service`，见 `pom.xml`）
- 分层结构约定：
  - `controller`：接口层（Spring MVC / WebFlux）
  - `service`：业务层
  - `repository`：数据访问封装
  - `mapper`：MyBatis-Plus Mapper 接口
  - `dto`：请求/响应 DTO
  - `model`：实体模型
  - `config`：配置类与 `@ConfigurationProperties`
  - `security` / `filter`：安全/网关过滤器
- 公共模块/通用组件位置：`common/src/main/java/com/easy/common`（`ApiResponse`、`ErrorCode`）
- 领域模型组织方式：DTO 在 `dto`；Entity/Model 在 `model`；VO/DO 未发现

### 2.3 前端目录结构（如存在）
- 未发现前端工程目录与 `package.json`

TODO(need-confirmation)：
- 前端工程是否在独立仓库或尚未初始化

---

## 3. 代码风格与规范
### 3.1 通用规范（General）
- 编码：UTF-8（`pom.xml` 中 `project.build.sourceEncoding`）
- 换行符：TODO(need-confirmation)（未发现 `.editorconfig`）
- 缩进：Java 源码普遍使用 4 空格（基于现有源码）
- 最大行宽：TODO(need-confirmation)
- 注释语言：中文（源码注释可见）
- 格式化/静态检查工具：未发现 `editorconfig/checkstyle/spotless/eslint/prettier`

### 3.2 命名规范（Naming Conventions）
#### 3.2.1 变量与方法命名
- 命名风格：camelCase
- 动词-名词规则：如 `createTokenPair`、`findByUsername`
- 禁止/避免的缩写：TODO(need-confirmation)

#### 3.2.2 类与文件命名
- 类命名：PascalCase
- 文件命名：与类同名（Java 约定）
- 类与文件的对应关系：一类一文件

#### 3.2.3 常量与枚举
- 常量命名：UPPER_SNAKE_CASE（如 `ErrorCode` 枚举值）
- 枚举命名与枚举值命名：枚举类 PascalCase，枚举值大写下划线

#### 3.2.4 数据库字段与 API 字段（如适用）
- 数据库字段命名：snake_case（如 `password_hash`、`created_at`）
- JSON 字段命名：camelCase（Jackson 默认）
- 前后端字段映射规则：`mybatis-plus.configuration.map-underscore-to-camel-case: true`

### 3.3 代码组织约定（Code Organization Rules）
- 单模块职责边界：`auth-service` 认证、`permission-service` 权限、`gateway-service` 网关、`common` 通用响应
- 允许的依赖方向：controller -> service -> repository -> mapper
- 禁止的写法：controller 直接调用 mapper；跨模块直接访问对方内部实现
- 错误处理与日志规范：
  - 统一响应：`ApiResponse` + `ErrorCode`
  - 统一异常处理：`GlobalExceptionHandler`（auth/permission）
  - 日志：使用 SLF4J API（如 `AuthGlobalFilter`）

TODO(need-confirmation)：
- 统一换行/行宽规范
- 日志实现与日志格式配置（未发现 logback/log4j 配置文件）

---

## 4. 构建与启动（Build & Run）
### 4.1 环境要求（Environment Requirements）
- 操作系统：Windows 11 + PowerShell
- 必需运行时：
    - JDK：17
    - Node.js：未发现前端工程（TODO(need-confirmation)）
    - 其他：Docker / Docker Compose（用于基础设施）
- 必需工具：Maven 3.9.9
- 关键环境变量（见 `.env`）：
  - `NACOS_ADDR`、`MYSQL_HOST`、`MYSQL_PORT`、`MYSQL_USER`、`MYSQL_PASSWORD`
  - `AUTH_MYSQL_DB`、`PERMISSION_MYSQL_DB`
  - `REDIS_HOST`、`REDIS_PORT`、`SENTINEL_DASHBOARD`、`JWT_SECRET`、`PERMISSION_SERVICE_URL`

### 4.2 后端构建与启动
> PowerShell 示例（可直接执行）
```powershell
# 构建全部服务
mvn -q -pl gateway-service,auth-service,permission-service -am -DskipTests package

# 分别启动
mvn -pl gateway-service spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl permission-service spring-boot:run
```

> 可选：启动基础设施（Docker Compose）
```powershell
docker compose up -d
```

### 4.3 前端构建与启动
- 未发现前端工程与脚本

TODO(need-confirmation)：
- 前端工程是否在独立仓库或尚未初始化

---

## 5. 主要依赖组件（Key Dependencies）
### 5.1 后端核心依赖
- 框架：Spring Boot 3.2.5（`pom.xml`）、Spring Cloud 2023.0.2、Spring Cloud Alibaba 2023.0.1.0
- ORM/数据库访问：MyBatis-Plus 3.5.6（auth: `mybatis-plus-boot-starter`；permission: `mybatis-plus-spring-boot3-starter`）
- 数据库：MySQL（`mysql-connector-j`；Docker 为 MySQL 8.0）
- 缓存：Redis（`spring-boot-starter-data-redis` / `spring-boot-starter-data-redis-reactive`）
- 鉴权/安全：JJWT 0.12.5 + `spring-security-crypto`（BCrypt）
- 接口文档：`springdoc-openapi-starter-webmvc-ui` 2.5.0（auth/permission）
- 监控：Micrometer Prometheus Registry

### 5.2 前端核心依赖
- 未发现前端依赖清单（无 `package.json`）

### 5.3 基础设施/中间件（如有）
- Docker Compose：`docker-compose.yml`
- 监控与治理：Prometheus、Grafana、Sentinel Dashboard
- 服务发现：Nacos（discovery）

TODO(need-confirmation)：
- 是否引入其他消息队列（Kafka/RabbitMQ）或任务调度组件
- 前端技术栈与依赖

---

## 6. 配置文件说明（Configuration）
### 6.1 全局配置
- 配置文件路径与命名：
  - `.env`：统一环境变量模板
  - `docker-compose.yml`：基础设施编排
  - `docker/prometheus.yml`：Prometheus 抓取配置
- 环境区分方式：未发现 `application-{profile}.yml`
- 敏感信息管理方式：`.env` 明文（JWT 密钥等）+ Spring 占位符

### 6.2 后端主要配置项
> 按服务分组列出关键项
- `auth-service/src/main/resources/application.yml`
  - `server.port=8081`
  - `spring.datasource`：MySQL（`AUTH_MYSQL_DB`）
  - `spring.data.redis`：Redis Host/Port
  - `spring.sql.init`：`schema.sql`/`data.sql`
  - `spring.cloud.nacos`：discovery/config（config 已禁用）
  - `app.auth`：`jwt-secret`、`access-token-expire-seconds`、`refresh-token-expire-seconds`
  - `mybatis-plus.configuration.map-underscore-to-camel-case=true`
- `permission-service/src/main/resources/application.yml`
  - `server.port=8082`
  - `spring.datasource`：MySQL（`PERMISSION_MYSQL_DB`）
  - `spring.data.redis`：Redis Host/Port
  - `spring.sql.init`：`schema.sql`/`data.sql`
  - `spring.cloud.nacos`：discovery/config（config 已禁用）
  - `app.permission.cache-ttl-seconds`
  - `mybatis-plus.configuration.map-underscore-to-camel-case=true`
- `gateway-service/src/main/resources/application.yml`
  - `server.port=8080`
  - `spring.cloud.gateway.routes`：`auth-service`/`permission-service` 路由
  - `filters: StripPrefix=2`（按当前配置）
  - `spring.cloud.sentinel.transport.dashboard`
  - `spring.data.redis`：Reactive Redis
  - `app.gateway.auth`：`jwt-secret`、`permission-service-url`、`whitelist-paths`（支持 `/**/api/auth/**`）

### 6.3 前端主要配置项
- 未发现前端 `.env*`、构建配置或代理配置

TODO(need-confirmation)：
- 是否启用 Spring Profiles 与环境覆盖策略
- 前端环境变量与代理配置（若有前端）

---

## 7. 面向 Agent 的开发规则（Development Rules for Agents）
> 以“必须 / 禁止 / 建议”的形式输出规则
- 必须：
  - 修改数据库结构或初始化数据时，同时更新对应模块的 `schema.sql` / `data.sql` / `model` / `mapper` / `repository`
  - 修改 API 契约时，同步更新对应 `dto` 与文档（`运行说明.md`/`设计文档.md`）
  - 新增依赖需更新对应模块 `pom.xml`，如涉及版本统一需更新父 `pom.xml` 的 `dependencyManagement`
  - 变更后至少通过 `mvn -pl <module> -DskipTests package`（PowerShell 可执行）
- 禁止：
  - 修改 `.git/` 与 `.idea/` 目录
  - 未经确认直接改动 `docker-compose.yml` / `.env` 的生产/共享配置项
  - 绕过现有分层直接在 controller 调用 mapper 或跨模块直接依赖内部实现
- 建议：
  - 公共响应与错误码统一使用 `common` 模块（`ApiResponse`、`ErrorCode`）
  - 网关鉴权与白名单规则统一在 `gateway-service` 的 `AuthGlobalFilter` 与配置中维护

TODO(need-confirmation)：
- 是否存在生成目录（如 `target/`、`dist/`、`vendor/`）需要明确“不可手工修改”的策略
- 是否需要强制的格式化/静态检查流程

---

## 8. 新业务与新功能开发规范（New Feature Development Rules）
### 8.1 技术栈使用约束（强制）
- 必须使用当前已存在的后端技术栈（Spring Boot/Spring Cloud/MyBatis-Plus/JJWT/Redis）
- 禁止为单一功能引入新的后端框架或重复能力的第三方库
- 如需新增依赖，必须说明原因并在 `pom.xml` 明确版本来源（父 POM 或模块 POM）

### 8.2 复用优先原则（强制）
- 必须优先复用现有能力与公共组件：
  - `common` 中的 `ApiResponse` 与 `ErrorCode`
  - `service`/`repository` 已存在的业务封装
  - 现有 JWT/Redis 安全组件（`JwtTokenService`、`RefreshTokenStore`、`AccessTokenBlacklistStore`）

### 8.3 新代码组织规范
- 新业务代码必须放入现有模块与分层目录中（controller/service/repository/mapper/dto/model）
- 禁止创建“临时目录”或绕过 `common` 模块复用公共能力
- 若新增服务模块：
  - 必须在父 `pom.xml` 的 `<modules>` 中声明
  - 必须补充 `docker-compose.yml`（如有新基础依赖）

### 8.4 实现方式与风格一致性
- 命名风格与当前代码一致（包名 `com.easy.*`、类名 PascalCase、方法名 camelCase）
- 异常处理统一通过 `GlobalExceptionHandler` 返回 `ApiResponse`
- 统一使用配置类 `@ConfigurationProperties` 管理新增配置

### 8.5 Agent 特别约束（重要）
- 未经明确指示不得引入新依赖或新技术方案
- 若现有能力不足，只能：
  - 标注 `TODO(need-confirmation)`
  - 说明缺失能力与影响范围
- 优先选择“最小改动、最大复用”的方案

TODO(need-confirmation)：
- 是否有前端新功能的约束规范（当前无前端工程）
