package com.zqc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zqc.domain.po.User;
import org.apache.ibatis.annotations.Param;
// import org.apache.ibatis.annotations.Update;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 自定义 SQL：扣减余额；WHERE 条件由 Wrapper 动态拼接（SQL 见 UserMapper.xml）
     */
    // @Update("UPDATE `user` SET balance = balance - #{amount} ${ew.customSqlSegment}")
    int deductBalance(@Param(Constants.WRAPPER) Wrapper<User> wrapper, @Param("amount") int amount);
}
