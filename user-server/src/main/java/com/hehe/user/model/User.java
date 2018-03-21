package com.hehe.user.model;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 *
 * @author xieqinghe .
 * @date 2017/11/14 下午4:43
 * @email qinghe101@qq.com
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
     * 正常：0，锁定：1
     */
    private Integer locked;

    /**
     * 账户类型 普通账户类型:1
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


    public enum Type {

        ORDINARY_USER(1, "普通用户");

        @Getter
        @Setter
        private Integer value;
        @Getter
        @Setter
        private String display;

        Type(Integer value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Type from(Integer value) {
            for (Type type : Type.values()) {
                if (Objects.equal(value, type.value)) {
                    return type;
                }
            }
            return null;
        }
    }

}
