package com.hehe.user.config.granter;

import com.hehe.common.event.CoreEventDispatcher;
import com.hehe.user.util.MyPasswordEncoder;
import org.slf4j.Logger;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieqinghe .
 * @date 2017/11/15 下午1:34
 * @email qinghe101@qq.com
 */
public class MyCompositeTokenGranter implements TokenGranter {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MyPasswordEncoder.class);



    /**
     * 异步执行，用来记录用户登录操作记录
     */
    private CoreEventDispatcher dispatcher;

    private final List<TokenGranter> tokenGranters;

    public MyCompositeTokenGranter(List<TokenGranter> tokenGranters, CoreEventDispatcher dispatcher) {
        this.tokenGranters = new ArrayList(tokenGranters);
        this.dispatcher = dispatcher;
    }

    @Override
    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        log.info("[COMPOSITE GRANTER] token_request client_id:{}, grant_type:{}, tokenRequest:{}", tokenRequest.getClientId(), grantType, tokenRequest.getRequestParameters());
        for (TokenGranter granter : tokenGranters) {
            OAuth2AccessToken grant = granter.grant(grantType, tokenRequest);
            if (grant!=null) {
                //dispatcher.publish(new UserEventLogsDto(tokenRequest,grant,"登录"))
                return grant;
            }
        }
        return null;
    }
}
