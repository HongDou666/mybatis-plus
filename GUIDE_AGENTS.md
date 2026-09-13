# mybatis-plus 工程说明

## 功能概述

本工程基于 Spring Boot + MyBatis-Plus，用于练习用户（User）与收货地址（Address）领域模型、数据访问与 REST 接口。用户 CRUD 与地址基本增删改查分别由 `IUserService` / `IAddressService` 封装；查用户时可经 `Db` 附带地址列表。统一响应为 `common.R`，业务异常为 `ResultCode` + `BizException`。`User.status` 使用枚举 `UserStatus` + `@EnumValue` 与库 int 自动转换。练习库名为 `mybatis_plus`。

`UserMapperTest` 除 BaseMapper CRUD 外，还覆盖 LambdaQueryWrapper / LambdaUpdateWrapper 条件示例（用户名 like + 余额 ge；按用户名更新余额）。另有自定义 SQL 示例：`deductBalance`（XML 扣减 + Wrapper WHERE）、`queryUsersByAddress`（user JOIN address；`city` 为 XML 参数，`u.id` 等由 Wrapper WHERE 注入，注解写法已注释保留）。

`application.yaml` 中已配置 MyBatis-Plus 常用项：`mapper-locations`、`type-aliases-package`、`map-underscore-to-camel-case`、stdout SQL 日志（`StdOutImpl`）、全局主键策略 `id-type: assign_id`、逻辑删除 `logic-delete-field/value`（与 `Address.@TableLogic` 配合）。数据源 JDBC URL 已开启 `rewriteBatchedStatements=true`，便于批量写入（如 `saveBatch`）被驱动重写为多值 SQL。

接口文档使用 Knife4j Next（OpenAPI3）Boot4 专用 starter：`com.baizhukui:knife4j-openapi3-boot4-spring-boot-starter`（当前版本 `5.6.1`；`5.7.1` 在当前镜像未能解析）。文档增强页地址：http://localhost:8080/doc.html ；OpenAPI 元信息由 `config/OpenApiConfig.java` 配置。

## REST 接口（UserController）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/users` | 新增用户，body 为 `UserFormDTO`，返回 `R.ok()` |
| DELETE | `/users/{id}` | 按 id 删除用户，返回 `R.ok()` |
| GET | `/users/{id}` | 按 id 查询用户（含收货地址列表），返回 `R.ok(UserVO)` |
| GET | `/users?ids=` | 按 id 列表批量查询（含各自收货地址），返回 `R.ok(List<UserVO>)` |
| PUT | `/users/balance/deduct?ids=&amount=` | 批量扣减余额（自定义 SQL） |
| PUT | `/users/{id}/deduction/{money}` | 按单个用户 id 扣减余额（自定义 SQL） |
| GET | `/users/by-address?city=&ids=` | 按城市+用户 id 关联 address 查询（自定义 SQL） |
| GET | `/users/list` | 复杂条件查询（name/status/minBalance/maxBalance，均可空） |

## REST 接口（AddressController）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/addresses` | 新增地址，body 为 `AddressFormDTO` |
| PUT | `/addresses` | 修改地址，body 含 id |
| DELETE | `/addresses/{id}` | 按 id 逻辑删除 |
| GET | `/addresses/{id}` | 按 id 查询 |
| GET | `/addresses?userId=` | 按用户 id 查地址列表 |

## 关键目录结构

```
mybatis-plus/
├── GUIDE_AGENTS.md              # 本文件：工程功能与目录说明
├── pom.xml                      # Maven 配置（Spring Boot + MyBatis-Plus + Knife4j Boot4）
├── sql/
│   └── mybatis_plus.sql           # 数据库初始化脚本（库名 mybatis_plus）
├── src/main/java/com/zqc/
│   ├── MybatisPlusApplication.java
│   ├── common/
│   │   ├── R.java               # 统一响应包装（code / msg / data）
│   │   ├── ResultCode.java      # 业务错误码枚举
│   │   └── exception/
│   │       ├── BizException.java            # 业务异常（带 code）
│   │       └── GlobalExceptionHandler.java  # 全局异常 → R
│   ├── config/
│   │   └── OpenApiConfig.java   # Knife4j / OpenAPI3 文档元信息
│   ├── controller/
│   │   ├── UserController.java     # 用户 REST
│   │   └── AddressController.java  # 地址基本增删改查
│   ├── domain/                  # po / dto / query / vo（User、Address）
│   ├── enums/
│   │   └── UserStatus.java      # 用户状态枚举（@EnumValue 与库 int 互转）
│   ├── mapper/                  # UserMapper、AddressMapper
│   └── service/
│       ├── IUserService.java
│       ├── IAddressService.java
│       └── impl/
│           ├── UserServiceImpl.java
│           └── AddressServiceImpl.java
├── src/main/resources/
│   ├── application.yaml         # 含 springdoc / knife4j；JDBC rewriteBatchedStatements
│   └── mapper/UserMapper.xml    # deductBalance 自定义 SQL（注解写法已注释）
└── src/test/java/com/zqc/
    ├── MybatisPlusApplicationTests.java
    └── mapper/UserMapperTest.java  # BaseMapper CRUD + Lambda 条件查询/更新示例
```
