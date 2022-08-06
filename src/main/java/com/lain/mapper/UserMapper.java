package com.lain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lain.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
