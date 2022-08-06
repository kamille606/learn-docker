package com.lain.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_user")
public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer sex;
    private Integer deleted;
    private Date updateTime;
    private Date createTime;
}
