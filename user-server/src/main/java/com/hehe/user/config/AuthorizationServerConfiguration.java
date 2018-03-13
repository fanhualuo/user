package com.hehe.user.config;

import com.hehe.common.event.CoreEventDispatcher;
import com.hehe.user.config.granter.MyClientDetailsService;
import com.hehe.user.config.granter.MyCompositeTokenGranter;
import com.hehe.user.config.granter.MyPasswordTokenGranter;
import com.hehe.user.config.granter.MyUserDetailsService;
import com.hehe.user.config.store.MyRedisTokenStore;
import com.hehe.user.service.ClientService;
import com.hehe.user.service.UserService;
import com.hehe.user.verification.EmailVerificationCodeServiceImpl;
import com.hehe.user.verification.SmsVerificationCodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author xieqinghe .
 * @date 2017/11/13 下午1:38
 * @email xieqinghe@terminus.io
 * 认证服务配置
 * 继承AuthorizationServerConfigurerAdapter类，
 * 添加@EnableAuthorizationServer注解
 * 主要是/oauth/token自定义配置、token存储方式等
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private CoreEventDispatcher dispatcher;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailVerificationCodeServiceImpl emailVerificationCodeService;

    @Autowired
    private SmsVerificationCodeServiceImpl smsVerificationCodeService;

    /**
     * 配置客户端数据
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.withClientDetails(new MyClientDetailsService(clientService));
    }

    /**
     * 配置授权服务器端点的非安全特性，如令牌存储、令牌
     * 自定义，用户认证和授权类型。默认可以不用任何配置
     * 密码授权，在这种情况下，你需要提供一个{@authenticationManager}。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //密码授权的身份验证管理,必须
        endpoints.authenticationManager(authenticationManager);
        //自定义token存储方式
        endpoints.tokenStore(tokenStore());
        //认证方式
        endpoints.tokenGranter(tokenGranter(clientDetailsService, tokenServices()));
        //code存储方式(生成code时使用配置源)
        endpoints.authorizationCodeServices(authorizationCodeServices());
    }

    /**
     * 配置授权服务器的安全性，必须配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        //允许表单认证
        oauthServer.allowFormAuthenticationForClients();
    }

    /**
     * token存储,这里使用已经实现的redis方式存储
     *
     * @param // accessTokenConverter
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        TokenStore tokenStore = new MyRedisTokenStore(redisConnectionFactory);
        return tokenStore;
    }

    /**
     * 自定义token获取配置
     *
     * @param
     * @return
     */
    public TokenGranter tokenGranter(ClientDetailsService clientDetailsService, AuthorizationServerTokenServices tokenServices) {
        List<TokenGranter> tokenGranters = new ArrayList<>();
        OAuth2RequestFactory requestFactory = requestFactory(clientDetailsService);

        //1，password 密码模式，重写（作为示例，其他模式都可以重写，具体可以参照spring源码）
        tokenGranters.add(new MyPasswordTokenGranter(tokenServices, clientDetailsService, requestFactory, userService, passwordEncoder, emailVerificationCodeService, smsVerificationCodeService));

        //2，authorization_code 授权码模式，spring security oauth2实现
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices(), clientDetailsService, requestFactory));

        //3，client_credentials 客户端模式，spring security oauth2实现
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));

        //4，implicit 简化模式，spring security oauth2实现
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory));

        //5, refresh_token 刷新模式 spring security oauth2实现  自己实现？
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));

        return new MyCompositeTokenGranter(tokenGranters, dispatcher);
    }

    @Bean
    public OAuth2RequestFactory requestFactory(ClientDetailsService clientDetailsService) {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    /**
     * 配置TokenServices参数
     * 参考spring源码实现，实现所需要类
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetailsService);
//        tokenServices.setTokenEnhancer(new TokenEnhancer() {
//            @Override
//            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//                String codeSource = (String) authentication.getOAuth2Request().getExtensions().get("from");
//                if (StringUtils.hasLength(codeSource)) {
//                    Map<String, Object> additionalInformation = Maps.newHashMap(accessToken.getAdditionalInformation());
//                    additionalInformation.put("source", codeSource);
//                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
//                }
//                return accessToken;
//            }
//        });
        addUserDetailsService(tokenServices, userDetailsService());
        return tokenServices;
    }

    /**
     * 参考spring源码实现，实现所需要类
     */
    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
                    userDetailsService));
            tokenServices
                    .setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        }
    }

    private UserDetailsService userDetailsService() {
        return new MyUserDetailsService(userService);
    }

    /**
     * 参考spring源码实现
     * code存储方式，授权码模式生成的code
     * 数据库表为 oauth_code，具体可以查看spring源码
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }
}
