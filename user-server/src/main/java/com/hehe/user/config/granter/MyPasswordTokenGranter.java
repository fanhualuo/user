package com.hehe.user.config.granter;

import com.hehe.common.model.Response;
import com.hehe.common.util.Arguments;
import com.hehe.user.model.User;
import com.hehe.user.service.UserService;
import com.hehe.user.util.GetAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 密码模式，重写
 * 比spring security oauth2 实现增加 手机验证码登录、邮箱验证码登录
 * @author xieqinghe .
 * @date 2017/11/15 下午3:27
 * @email xieqinghe@terminus.io
 */
public class MyPasswordTokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "password";


    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public MyPasswordTokenGranter(AuthorizationServerTokenServices tokenServices,
                                  ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory requestFactory,
                                  UserService userService,
                                  PasswordEncoder passwordEncoder) {
        this( tokenServices, clientDetailsService, requestFactory, GRANT_TYPE, userService,passwordEncoder);
    }

    protected MyPasswordTokenGranter(AuthorizationServerTokenServices tokenServices,
                                     ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType, UserService userService, PasswordEncoder passwordEncoder) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");
        // Protect from downstream leaks of password
        parameters.remove("password");

        String connection = parameters.get("connection");

        if (!StringUtils.hasText(connection)) {
            throw new InvalidRequestException("connection not specified");
        }
        if (!StringUtils.hasText(password)) {
            throw new InvalidRequestException("password not specified");
        }
        Authentication userAuth;
        //普通登录
        if (connection.equals("common_password")){
            User user;
            Response<User> response= userService.findByIdentity(username);
            if (!response.isSuccess()|| Arguments.isNull(response.getResult())){
                throw new ClientAuthenticationException("username.not.found") {
                    @Override
                    public String getOAuth2ErrorCode() {
                        return "username.not.found";
                    }
                };
            }
            user=response.getResult();
            //密码校验
            if (!passwordEncoder.matches(password,user.getPassword())){
                throw new ClientAuthenticationException("bad_credentials") {
                    @Override
                    public String getOAuth2ErrorCode() {
                        return "bad_credentials";
                    }
                };
            }
            GetAuthentication getAuthentication=new GetAuthentication();
            userAuth = getAuthentication.buildToken(user);
        }else {
            throw new InvalidRequestException("connection.not.legal");
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);

    }

}
