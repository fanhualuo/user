package com.hehe.user.web.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.hehe.common.util.Arguments;
import com.hehe.common.util.JsonResponseException;
import com.hehe.common.util.RespHelper;
import com.hehe.common.util.VerifyUtil;
import com.hehe.user.model.User;
import com.hehe.user.service.UserService;
import com.hehe.user.verification.EmailVerificationCodeServiceImpl;
import com.hehe.user.verification.SmsVerificationCodeServiceImpl;
import com.hehe.user.verification.model.VerificationCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.Serializable;

/**
 * 用户注册相关
 *
 * @author xieqinghe .
 * @date 2018/3/8 下午3:50
 * @email qinghe101@qq.com
 */
@RestController
@EnableSwagger2
@Slf4j
@RequestMapping("/api/user/v1")
public class UserSignupApiController {

    @Autowired
    private EmailVerificationCodeServiceImpl emailVerificationCodeService;

    @Autowired
    private SmsVerificationCodeServiceImpl smsVerificationCodeService;


    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * 邮箱、短信 --> 注册、登录、密码重置
     *
     * @param
     * @return
     */
    @PostMapping("/send/code")
    public String sendVerificationCode(@RequestBody VerificationCodeJson verificationCodeJson, OAuth2Authentication auth) {

        if (!auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        if (Strings.isNullOrEmpty(verificationCodeJson.getMode())) {
            throw new JsonResponseException(400, "send.code.mode.not.null");
        }
        VerificationCode.Type type = VerificationCode.Type.from(verificationCodeJson.getCodeType());
        if (Arguments.isNull(type)) {
            throw new JsonResponseException(400, "send.code.type.not.null");
        }
        //邮箱
        if (Objects.equal(verificationCodeJson.getMode(), "email")) {
            if (Strings.isNullOrEmpty(verificationCodeJson.getIdentity()) || !VerifyUtil.verifyEmail(verificationCodeJson.getIdentity())) {
                throw new JsonResponseException(400, "user.email.not.legal");
            }
            User user = RespHelper.or500(userService.findByEmail(verificationCodeJson.getIdentity()));
            //注册，必须不存在此用户
            if (type.equals(VerificationCode.Type.REGISTER)) {
                if (Arguments.notNull((user))) {
                    throw new JsonResponseException(400, "user.already.exist");
                }
            } else if (Arguments.isNull(user)) {
                throw new JsonResponseException(400, "username.not.found");
            }
            return RespHelper.or500(emailVerificationCodeService.sendVerificationCode(verificationCodeJson.getIdentity(), type));

        } else if (Objects.equal(verificationCodeJson.getMode(), "sms")) {
            //手机
            if (Strings.isNullOrEmpty(verificationCodeJson.getIdentity()) || !VerifyUtil.verifyMobile(verificationCodeJson.getIdentity())) {
                throw new JsonResponseException(400, "user.phone.not.legal");
            }
            User user = RespHelper.or500(userService.findByPhone(verificationCodeJson.getIdentity()));
            if (type.getDisplay().equals(VerificationCode.Type.REGISTER)) {
                if (Arguments.notNull((user))) {
                    throw new JsonResponseException(400, "user.already.exist");
                }
            } else if (user == null) {
                throw new JsonResponseException(400, "username.not.found");
            }
            return RespHelper.or500(smsVerificationCodeService.sendVerificationCode(verificationCodeJson.getIdentity(), type));

        } else {
            throw new JsonResponseException(400, "send.code.mode.not.null");
        }
    }


    /**
     * 注册页面
     * 邮箱、短信验证码注册
     *
     * @param
     * @return
     */
    @PostMapping("/signup")
    public String signup(@RequestBody SignupJson signupJson, OAuth2Authentication auth) {
        if (!auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        if (Strings.isNullOrEmpty(signupJson.getMode())) {
            throw new JsonResponseException(400, "signup.code.mode.not.null");
        }
        //
        if (Strings.isNullOrEmpty(signupJson.getPassword())) {
            throw new JsonResponseException(400, "user.password.not.null");
        }
        if (Strings.isNullOrEmpty(signupJson.getCode())) {
            throw new JsonResponseException(400, "user.code.not.null");
        }
        if (signupJson.getPassword().length() < 8) {
            throw new JsonResponseException(400, "user.password.not.legal");
        }
        User user = new User();
        //邮箱
        if (Objects.equal(signupJson.getMode(), "email")) {
            if (Strings.isNullOrEmpty(signupJson.getIdentity()) || !VerifyUtil.verifyEmail(signupJson.getIdentity())) {
                throw new JsonResponseException(400, "user.email.not.legal");
            }
            User user1 = RespHelper.or500(userService.findByEmail(signupJson.getIdentity()));
            //注册，必须不存在此用户
            if (Arguments.notNull((user1))) {
                throw new JsonResponseException(400, "user.already.exist");
            }
            if (!emailVerificationCodeService.verificationCode(VerificationCode.Type.REGISTER, signupJson.getIdentity(), signupJson.getCode())) {
                throw new JsonResponseException(400, "user.code.is.error");
            }
            user.setEmail(signupJson.getIdentity());
        } else if (Objects.equal(signupJson.getMode(), "sms")) {
            //手机
            if (Strings.isNullOrEmpty(signupJson.getIdentity()) || !VerifyUtil.verifyMobile(signupJson.getIdentity())) {
                throw new JsonResponseException(400, "user.phone.not.legal");
            }
            User user1 = RespHelper.or500(userService.findByPhone(signupJson.getIdentity()));
            if (Arguments.notNull((user1))) {
                throw new JsonResponseException(400, "user.already.exist");
            }
            if (!smsVerificationCodeService.verificationCode(VerificationCode.Type.REGISTER, signupJson.getIdentity(), signupJson.getCode())) {
                throw new JsonResponseException(400, "user.code.is.error");
            }
            user.setPhone(signupJson.getIdentity());
        } else {
            throw new JsonResponseException(400, "send.code.mode.not.null");
        }

        user.setPassword(signupJson.getPassword());
        RespHelper.or500(userService.create(user));
        return "success";
    }


    @Data
    public static class VerificationCodeJson implements Serializable {

        private static final long serialVersionUID = 8004908191047591768L;
        /**
         * 手机/邮箱
         */
        private String identity;

        /**
         * 验证码类型 1, "注册"、 2, "登录"、 3, "密码重置"
         */
        @JsonProperty("code_type")
        private Integer codeType;

        /**
         * 验证方式  邮箱：email，手机短信：sms
         */
        private String mode;

        /**
         * 验证码token
         */
        @JsonProperty("captcha_token")
        private String captchaToken;

        /**
         * 验证码答案
         */
        @JsonProperty("captcha_answer")
        private String captchaAnswer;
    }


    @Data
    public static class SignupJson implements Serializable {
        private static final long serialVersionUID = 9125285746062287723L;
        /**
         * 手机/邮箱
         */
        private String identity;

        /**
         * 验证方式  邮箱：email，手机短信：sms
         */
        private String mode;

        /**
         * 验证码
         */
        private String code;
        /**
         * 验证码token
         */
        @JsonProperty("captcha_token")
        private String captchaToken;

        /**
         * 验证码答案
         */
        @JsonProperty("captcha_answer")
        private String captchaAnswer;

        /**
         * 密码
         */
        private String password;


    }

}
