package com.hehe.user.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.hehe.common.util.Arguments;
import com.hehe.common.util.JsonMapper;
import com.hehe.common.util.JsonResponseException;
import com.hehe.common.util.RespHelper;
import com.hehe.user.dto.UserInfoDetailedDto;
import com.hehe.user.dto.UserStandardDto;
import com.hehe.user.model.User;
import com.hehe.user.model.UserDetail;
import com.hehe.user.service.UserDetailService;
import com.hehe.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Map;

import static com.hehe.common.util.MessageUtil.SUCCESS;

/**
 * 用户信息
 *
 * @author xieqinghe .
 * @date 2018/3/16 下午2:35
 * @email qinghe101@qq.com
 */
@RestController
@EnableSwagger2
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailService userDetailService;

    /**
     * 查询用户基本信息
     *
     * @param
     * @return
     */
    @GetMapping("/find/info/standard")
    public UserStandardDto findUserStandard(OAuth2Authentication auth) {
        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = RespHelper.or500(userService.findById(userId));
        User.Type type = User.Type.from(user.getType());
        String userType = (type != null ? type.getDisplay() : "");
        return new UserStandardDto(user.getId(), user.getUsername(), userType, user.getAvatarUrl());
    }

    /**
     * 查询用户全量信息
     *
     * @param
     * @return
     */
    @GetMapping("/find/info/full")
    public Map<String, Object> findUserFull(OAuth2Authentication auth) {
        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = RespHelper.or500(userService.findById(userId));
        //屏蔽密码
        user.setPassword(null);
        UserDetail userDetail = RespHelper.or500(userDetailService.findByUserId(userId));

        Map<String, Object> map = Maps.newHashMap();
        map.put("user", user);
        if (userDetail != null) {
            UserInfoDetailedDto userInfoDetailedDto = JsonMapper.JSON_NON_DEFAULT_MAPPER
                    .fromJson(userDetail.getInfoDetailed(), UserInfoDetailedDto.class);
            map.put("InfoDetailed", userInfoDetailedDto);
        }
        return map;
    }

    /**
     * 修改用户详细信息
     *
     * @param
     * @return
     */
    @PostMapping("/update/info/detail")
    public String updateUserDetail(@RequestBody UserInfoDetailedDto userInfoDetailedDto, OAuth2Authentication auth) {
        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        UserDetail userDetail = RespHelper.or500(userDetailService.findByUserId(userId));

        //查询用户详情为空
        if (Arguments.isNull(userDetail)) {
            UserDetail userDetail1 = new UserDetail();
            userDetail1.setUserId(userId);
            userDetail1.setInfoDetailed(JsonMapper.JSON_NON_DEFAULT_MAPPER.toJson(userInfoDetailedDto));
            RespHelper.or500(userDetailService.create(userDetail1));
        } else {
            userDetail.setInfoDetailed(JsonMapper.JSON_NON_DEFAULT_MAPPER.toJson(userInfoDetailedDto));
            RespHelper.or500(userDetailService.update(userDetail));
        }
        return SUCCESS;
    }

    /**
     * 设置用户名，一个用户只能修改一次
     *
     * @param
     * @return
     */
    @PostMapping("/update/username")
    public String updateUserStandard(OAuth2Authentication auth,
                                     @RequestParam("username") String username) {
        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        if (Strings.isNullOrEmpty(username)){
            throw new JsonResponseException(400, "user.identity.not.null");
        }
        //判断用户名是否存在
        if (Arguments.notNull(RespHelper.or500(userService.findByUserName(username)))) {
            throw new JsonResponseException(400, "user.username.already.exist");
        }
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = RespHelper.or500(userService.findById(userId));
        //对于已经存在用户名的用户，不允许修改用户名
        if (!Strings.isNullOrEmpty(user.getUsername())){
            throw new JsonResponseException(400, "user.username.already.setup");
        }

        User user1 = new User();
        user1.setId(user.getId());
        user1.setUsername(username);

        RespHelper.or500(userService.update(user1));
        return SUCCESS;
    }

    /**
     * 修改用户头像
     *
     * @param
     * @return
     */
    @PostMapping("/update/info/avatar-url")
    public String updateUserAvatarUrl(OAuth2Authentication auth,
                                      @RequestParam("avatarUrl") String avatarUrl) {
        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }

        if (!StringUtils.hasText(avatarUrl)) {
            throw new JsonResponseException(400, "user.avatar.url.already.exist");
        }

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = RespHelper.or500(userService.findById(userId));

        User user1 = new User();
        user1.setId(user.getId());
        user1.setAvatarUrl(avatarUrl);
        RespHelper.or500(userService.update(user1));
        return SUCCESS;
    }
}


