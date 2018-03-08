package com.hehe.user.web.controller;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xieqinghe .
 * @date 2017/11/7 上午11:33
 * @email xieqinghe@terminus.io
 */
@RestController
public class TestController {

    @GetMapping("/v2/test")
    public String success() {
        return "test v2";
    }


    @GetMapping("/v1/test")
    public String test2(OAuth2Authentication auth) {
        return "test v1 ";
    }

    @GetMapping("/v3/test")
    public String test3(OAuth2Authentication auth) {
        return "test v3 ";
    }

    @GetMapping("/error")
    public String error() {
        return "error.html";
    }
}
