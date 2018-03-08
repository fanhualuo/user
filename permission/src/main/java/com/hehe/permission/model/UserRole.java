package com.hehe.permission.model;

import lombok.Data;

import java.util.Date;

/**
 * @author xieqinghe .
 * @date 2018/2/23 上午9:58
 * @email xieqinghe@terminus.io
 */
@Data
public class UserRole {
    Long id;

    String roleKey;

    Long userId;

    Long targetId;

    Role role;

    Date createdAt;

    Date updatedAt;

}
