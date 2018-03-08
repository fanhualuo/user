package com.hehe.permission.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xieqinghe .
 * @date 2018/2/23 上午9:54
 * @email xieqinghe@terminus.io
 */
@Data
public class Role {

    private String key;

    private String scope;

    private String name;

    private String description;

    private List<String> permissions;

    private Date createdAt;

    private Date updatedAt;

}
