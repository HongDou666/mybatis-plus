package com.zqc.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 插件配置。
 * <p>
 * 未注册分页插件时，调用 IService.page / BaseMapper.selectPage 等分页方法
 * 不会自动改写 SQL（不会拼 LIMIT、也不会查 total），结果往往等同于全量查询。
 * 因此需要通过 {@link MybatisPlusInterceptor} 挂载 {@link PaginationInnerInterceptor}，
 * 由插件在执行前拦截并按数据库方言生成分页 SQL。
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 注册 MyBatis-Plus 拦截器，并加入 MySQL 分页内插件。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // DbType.MYSQL：按 MySQL 方言生成 LIMIT 分页语句（与本工程数据源一致）
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
