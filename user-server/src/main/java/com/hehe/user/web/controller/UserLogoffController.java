package com.hehe.user.web.controller;

import com.hehe.common.util.JsonResponseException;
import com.hehe.common.util.RespHelper;
import com.hehe.user.model.User;
import com.hehe.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xieqinghe .
 * @date 2018/3/16 下午2:38
 * @email qinghe101@qq.com
 */
@RestController
@EnableSwagger2
@Slf4j
public class UserLogoffController {


    @Autowired
    private UserService userService;

    /**
     * 注销用户(个人级别token)
     *
     * @param
     * @return 注销成功返回true，否则返回false
     */
    @DeleteMapping("/api/user/v1/logoff/user")
    public Boolean logoffUser(OAuth2Authentication auth) {
        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = RespHelper.or500(userService.findById(userId));
        log.info("logoff user by id:{} ", user);
        return RespHelper.or500(userService.delete(userId));
    }
}
