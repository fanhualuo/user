package com.hehe.user.web.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hehe.common.model.Response;
import com.hehe.common.util.Arguments;
import com.hehe.common.util.JsonResponseException;
import com.hehe.common.util.ServiceException;
import com.hehe.user.model.User;
import com.hehe.user.service.UserService;
import com.hehe.user.util.GetAuthentication;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

/**
 * 登录页面
 *
 * @author xieqinghe .
 * @date 2018/1/13 下午7:15
 * @email qinghe101@qq.com
 */
@Controller
@EnableSwagger2
public class UserLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationEventPublisher authenticationEventPublisher;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @GetMapping("/")
    public String index() {
        return "success";
    }

    @GetMapping("/login")
    public String login() {
        return "view/login";
    }

    @PostMapping("/login/login")
    @ResponseBody
    public Map<String,Object> login(@RequestBody PasswordBody body, WebRequest webRequest, HttpServletRequest request, HttpServletResponse response) {
        try {

            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            request.getSession(true);
            //验证码校验
            if (Arguments.notNull(body.getCaptchaAnswer())) {

            }
            User user = checkUserPassword(body.getUsername(), body.getPassword());

            GetAuthentication getAuthentication = new GetAuthentication();
            Authentication authResult = getAuthentication.buildToken(user);
            String redirect = afterLoginSuccess(authResult, webRequest, request, response, body.getClientId(), body.getRedirectUri(), body.getState());

            //return "redirect:/";
            return Collections.singletonMap("redirect", redirect);

        } catch (JsonResponseException|ServiceException e) {
            throw new JsonResponseException(401,e.getMessage());
        }catch (Exception e) {
            throw new JsonResponseException(401,"server.unknown.error");
        }
    }

    private String afterLoginSuccess(Authentication auth, WebRequest webRequest, HttpServletRequest request, HttpServletResponse response, String clientId, String redirectUri, String state) {
        SecurityContextHolder.getContext().setAuthentication(auth);
        authenticationEventPublisher.publishAuthenticationSuccess(auth);
        HttpSession session = request.getSession();
        // 这个非常重要，否则验证后将无法登陆
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        if (StringUtils.hasText(clientId) && StringUtils.hasText(redirectUri)) {
            String redirect = "/oauth/authorize?client_id=" + clientId + "&response_type=code";
            redirect += "&redirect_uri=" + redirectUri;
            if (StringUtils.hasText(state)) {
                redirect += "&state=" + state;
            }
            return redirect;
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            return "/";
        } else {
            return savedRequest.getRedirectUrl();
        }
    }

    /**
     * 检验密码
     */
    private User checkUserPassword(String username, String password) {
        if (!StringUtils.hasText(password)) {
            throw new JsonResponseException(401, "user.password.not.null");
        }
        Response<User> resp = userService.findByIdentity(username);
        if (!resp.isSuccess()) {
            throw new JsonResponseException(401, resp.getError());
        }
//        if (!passwordEncoder.matches(resp.getResult().getPassword(), password)) {
//            throw new JsonResponseException(401, "user.password.incorrect");
//        }
        return resp.getResult();
    }

    @Data
    private static class PasswordBody {

        private String username;

        private String password;

        @JsonProperty("captcha_token")
        private String captchaToken;

        @JsonProperty("captcha_answer")
        private String captchaAnswer;

        @JsonProperty("client_id")
        private String clientId;

        @JsonProperty("redirect_uri")
        private String redirectUri;

        private String state;
    }


}
