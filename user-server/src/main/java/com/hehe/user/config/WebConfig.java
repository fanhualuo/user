package com.hehe.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author xieqinghe .
 * @date 2018/1/14 下午4:54
 * @email xieqinghe@terminus.io
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     *
     * 配置国际化
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageResource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages_zh_CN");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
