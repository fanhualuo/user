package com.hehe.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户详细信息
 *
 * @author xieqinghe .
 * @date 2018/3/16 下午3:02
 * @email qinghe101@qq.com
 */
@Data
public class UserDetail implements Serializable {
    private static final long serialVersionUID = -5141634452453936819L;

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户详细信息、json字符串
     */
    private String infoDetailed;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

}



