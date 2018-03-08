package com.hehe.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 *
 * @author xieqinghe .
 * @date 2017/11/14 下午4:43
 * @email xieqinghe@terminus.io
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 6799471578774375581L;
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 加密密码
     */
    private String password;

    private Integer enabled;

    /**
     *锁定
     */
    private Integer locked;

    /**
     *账户类型 普通账户类型:1
     */
    private Integer type;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
