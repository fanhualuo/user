package com.hehe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户标准信息
 *
 * @author xieqinghe .
 * @date 2018/3/16 下午3:54
 * @email qinghe101@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStandardDto implements Serializable {
    private static final long serialVersionUID = 6532098932613872862L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称、唯一
     */
    private String username;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 头像url
     */
    private String avatarUrl;


}
