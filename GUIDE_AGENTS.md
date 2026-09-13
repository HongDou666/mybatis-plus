# mybatis-plus 工程说明

## 功能概述

本工程基于 Spring Boot + MyBatis-Plus，用于练习用户（User）领域模型、数据访问与 REST 接口。用户 CRUD 通过 `UserMapper` 继承 MyBatis-Plus `BaseMapper<User>` 的通用方法完成（如 `insert`、`selectById`、`selectBatchIds`、`updateById`、`deleteById`），业务层由 `IUserService` / `UserServiceImpl`（继承 `ServiceImpl`）封装，对外由 `UserController` 提供 REST 接口（含通用 CRUD 与自定义 SQL：扣减余额、按地址关联查询），统一响应包装为 `common.R`。业务异常使用 `ResultCode` + `BizException`，由 `GlobalExceptionHandler` 统一转换为 `R`。User 主键使用 MyBatis-Plus 雪花算法（`IdType.ASSIGN_ID`），建表脚本 `user.id` 无 `AUTO_INCREMENT`。练习库名为 `mybatis_plus`。

`UserMapperTest` 除 BaseMapper CRUD 外，还覆盖 LambdaQueryWrapper / LambdaUpdateWrapper 条件示例（用户名 like + 余额 ge；按用户名更新余额）。另有自定义 SQL 示例：`deductBalance`（XML 扣减 + Wrapper WHERE）、`queryUsersByAddress`（user JOIN address；`city` 为 XML 参数，`u.id` 等由 Wrapper WHERE 注入，注解写法已注释保留）。

`application.yaml` 中已配置 MyBatis-Plus 常用项：`mapper-locations`、`type-aliases-package`、`map-underscore-to-camel-case`、stdout SQL 日志（`StdOutImpl`）、全局主键策略 `id-type: assign_id`（未启用逻辑删除，User 表暂无 deleted 字段）。数据源 JDBC URL 已开启 `rewriteBatchedStatements=true`，便于批量写入（如 `saveBatch`）被驱动重写为多值 SQL。

接口文档使用 Knife4j Next（OpenAPI3）Boot4 专用 starter：`com.baizhukui:knife4j-openapi3-boot4-spring-boot-starter`（当前版本 `5.6.1`；`5.7.1` 在当前镜像未能解析）。文档增强页地址：http://localhost:8080/doc.html ；OpenAPI 元信息由 `config/OpenApiConfig.java` 配置。

## REST 接口（UserController）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/users` | 新增用户，body 为 `UserFormDTO`，返回 `R.ok()` |
| DELETE | `/users/{id}` | 按 id 删除用户，返回 `R.ok()` |
| GET | `/users/{id}` | 按 id 查询用户（含收货地址列表），返回 `R.ok(UserVO)` |
| GET | `/users?ids=` | 按 id 列表批量查询，返回 `R.ok(List<UserVO>)` |
| PUT | `/users/balance/deduct?ids=&amount=` | 批量扣减余额（自定义 SQL） |
| PUT | `/users/{id}/deduction/{money}` | 按单个用户 id 扣减余额（自定义 SQL） |
| GET | `/users/by-address?city=&ids=` | 按城市+用户 id 关联 address 查询（自定义 SQL） |
| GET | `/users/list` | 复杂条件查询（name/status/minBalance/maxBalance，均可空） |

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
│   │   └── UserController.java  # 用户 REST：增删查（单/批量）+ OpenAPI 注解
│   ├── domain/                  # po / dto / query / vo（User、Address；UserVO 含 addresses）
│   ├── mapper/                  # UserMapper、AddressMapper（Db 查地址用）
│   └── service/
│       ├── IUserService.java    # 用户业务接口（继承 spring.service.IService<User>）
│       └── impl/
│           └── UserServiceImpl.java  # 含 Db.lambdaQuery(Address) 避免循环依赖
├── src/main/resources/
│   ├── application.yaml         # 含 springdoc / knife4j；JDBC rewriteBatchedStatements
│   └── mapper/UserMapper.xml    # deductBalance 自定义 SQL（注解写法已注释）
└── src/test/java/com/zqc/
    ├── MybatisPlusApplicationTests.java
    └── mapper/UserMapperTest.java  # BaseMapper CRUD + Lambda 条件查询/更新示例
```
