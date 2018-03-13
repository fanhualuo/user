package com.hehe.user.config;

import com.hehe.user.config.granter.MyUserDetailsService;
import com.hehe.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * 配置spring security
 *
 * @author xieqinghe .
 * @date 2017/11/7 上午11:51
 * @email qinghe101@qq.com
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .requestMatchers().anyRequest()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/authorize").permitAll()
                .antMatchers("/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
                //.formLogin().loginPage("/login.html").loginProcessingUrl("/login").defaultSuccessUrl("/success.html").failureUrl("/error.html").usernameParameter("username").passwordParameter("password").permitAll()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable();
    }


    /**
     * 重写框架的UserDetailsService接口，自定义用户存储接口
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new MyUserDetailsService(userService);
    }

}
