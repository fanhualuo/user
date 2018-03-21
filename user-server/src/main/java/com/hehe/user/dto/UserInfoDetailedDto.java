package com.hehe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户详细信息
 *
 * @author xieqinghe .
 * @date 2018/3/16 下午3:18
 * @email qinghe101@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDetailedDto implements Serializable {
    private static final long serialVersionUID = 8794258719231998326L;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 所在地
     */
    private String address;

    /**
     * 工作单位
     */
    private String workUnit;
    /**
     * 身份证号
     */
    private String idNumber;
}
