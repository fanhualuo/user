package com.hehe.user.verification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hehe
 * @date 2017/8/31 下午4:12
 * @email qinghe101@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {


    /** 发件人 **/
    private String fromAddress;

    /** 收件人 **/
    private String toAddress;

    /** 邮件主题 **/
    private String subject;

    /** 邮件内容 **/
    private String content;

}
