package com.hehe.user.event;

import lombok.Data;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.TokenRequest;

/**
 * @author xieqinghe .
 * @date 2017/11/15 下午5:26
 * @email qinghe101@qq.com
 */
@Data
public class UserEventLogsDto {
    private TokenRequest tokenRequest;
    private OAuth2AccessToken grant;
    private String desc;
    public UserEventLogsDto(TokenRequest tokenRequest, OAuth2AccessToken grant, String desc){
        this.tokenRequest=tokenRequest;
        this.grant=grant;
        this.desc=desc;
    }
}
