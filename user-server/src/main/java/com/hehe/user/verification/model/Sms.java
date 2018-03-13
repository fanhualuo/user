package com.hehe.user.verification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发送短信验证码
 * @author hehe
 * @date 2017/8/31 下午4:12
 * @email qinghe101@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sms implements Serializable {

    private static final long serialVersionUID = 8846057336499688590L;
    /** 收件人 **/
    private String toPhone;

    /** 邮件内容 **/
    private String content;

}
