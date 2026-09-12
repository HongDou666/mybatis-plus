# mybatis-plus 工程说明

## 功能概述

本工程基于 Spring Boot + MyBatis-Plus，用于练习用户（User）领域模型与数据访问。用户 CRUD 通过 `UserMapper` 继承 MyBatis-Plus `BaseMapper<User>` 的通用方法完成（如 `insert`、`selectById`、`selectBatchIds`、`updateById`、`deleteById`），不再依赖自定义 XML CRUD SQL。User 主键使用 MyBatis-Plus 雪花算法（`IdType.ASSIGN_ID`），建表脚本 `user.id` 无 `AUTO_INCREMENT`。练习库名为 `mybatis_plus`。

`UserMapperTest` 除 BaseMapper CRUD 外，还覆盖 LambdaQueryWrapper / LambdaUpdateWrapper 条件示例（用户名 like + 余额 ge；按用户名更新余额）。另有自定义 SQL 示例：`deductBalance`（XML 扣减 + Wrapper WHERE）、`queryUsersByAddress`（user JOIN address + Wrapper WHERE，注解写法已注释保留）。

`application.yaml` 中已配置 MyBatis-Plus 常用项：`mapper-locations`、`type-aliases-package`、`map-underscore-to-camel-case`、stdout SQL 日志（`StdOutImpl`）、全局主键策略 `id-type: assign_id`（未启用逻辑删除，User 表暂无 deleted 字段）。

## 关键目录结构

```
mybatis-plus/
├── GUIDE_AGENTS.md              # 本文件：工程功能与目录说明
├── pom.xml                      # Maven 配置（Spring Boot + MyBatis-Plus starter）
├── sql/
│   └── mybatis_plus.sql           # 数据库初始化脚本（库名 mybatis_plus）
├── src/main/java/com/zqc/
│   ├── MybatisPlusApplication.java
│   ├── domain/                  # po / dto / query / vo（User 实体含 @TableName / @TableId）
│   └── mapper/                  # UserMapper extends BaseMapper + deductBalance 自定义 SQL
├── src/main/resources/
│   ├── application.yaml
│   └── mapper/UserMapper.xml    # deductBalance 自定义 SQL（注解写法已注释）
└── src/test/java/com/zqc/
    ├── MybatisPlusApplicationTests.java
    └── mapper/UserMapperTest.java  # BaseMapper CRUD + Lambda 条件查询/更新示例
```
