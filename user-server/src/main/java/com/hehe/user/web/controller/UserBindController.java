package com.hehe.user.web.controller;

import com.google.common.base.Strings;
import com.hehe.common.util.Arguments;
import com.hehe.common.util.JsonResponseException;
import com.hehe.common.util.RespHelper;
import com.hehe.user.model.User;
import com.hehe.user.service.UserService;
import com.hehe.user.verification.EmailVerificationCodeServiceImpl;
import com.hehe.user.verification.SmsVerificationCodeServiceImpl;
import com.hehe.user.verification.model.VerificationCode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.Serializable;

import static com.hehe.common.util.MessageUtil.SUCCESS;
import static com.hehe.common.util.VerifyUtil.verifyEmail;
import static com.hehe.common.util.VerifyUtil.verifyMobile;

/**
 * 用户绑定解绑
 *
 * @author xieqinghe .
 * @date 2018/3/20 下午4:46
 * @email xieqinghe@terminus.io
 */
@RestController
@EnableSwagger2
@RequestMapping("/api/user/v1")
public class UserBindController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailVerificationCodeServiceImpl emailVerificationCodeService;

    @Autowired
    private SmsVerificationCodeServiceImpl smsVerificationCodeService;


    //  绑定/解绑 邮箱/手机号策略：通过手机/邮箱验证码进行绑定/解绑
    //  并且如果手机号和邮箱只存在一项，则不能解除绑定

    /**
     * 绑定手机号
     *
     * @param
     * @return
     */
    @PostMapping("/bind/phone")
    public String bindPhone(@RequestBody BindRequest bindRequest,
                            OAuth2Authentication auth) {

        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        checkPhone(bindRequest);
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = RespHelper.or500(userService.findById(userId));
        //判断是否已经绑定
        if (!Strings.isNullOrEmpty(user.getPhone())) {
            throw new JsonResponseException(400, "user.phone.already.bind");
        }

        User user1 = RespHelper.or500(userService.findByPhone(bindRequest.getIdentity()));
        if (Arguments.notNull(user1)) {
            throw new JsonResponseException(400, "user.phone.already.other.bind");
        }
        //校验验证码是否正确
        if (!smsVerificationCodeService.verificationCode(
                VerificationCode.Type.BINGING, bindRequest.getIdentity(), bindRequest.getCode())) {
            throw new JsonResponseException(400, "user.code.is.error");
        }

        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setPhone(bindRequest.getIdentity());
        RespHelper.or500(userService.updatePhone(userUpdate));

        //删除验证码
        smsVerificationCodeService.invalidVerificationCode(VerificationCode.Type.BINGING, bindRequest.getIdentity());
        return SUCCESS;
    }

    /**
     * 解绑手机号
     *
     * @param
     * @return
     */
    @PostMapping("/unbind/phone")
    public String unBindPhone(@RequestBody BindRequest bindRequest,
                              OAuth2Authentication auth) {

        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        checkPhone(bindRequest);
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        //校验验证码是否正确
        if (!smsVerificationCodeService.verificationCode(
                VerificationCode.Type.UN_BINGING, bindRequest.getIdentity(), bindRequest.getCode())) {
            throw new JsonResponseException(400, "user.code.is.error");
        }

        User userUpdate = new User();
        userUpdate.setId(userId);
        userUpdate.setPhone(null);
        RespHelper.or500(userService.updatePhone(userUpdate));

        //删除验证码
        smsVerificationCodeService.invalidVerificationCode(VerificationCode.Type.UN_BINGING, bindRequest.getIdentity());
        return SUCCESS;
    }

    /**
     * 绑定邮箱
     *
     * @param
     * @return
     */
    @PostMapping("/bind/email")
    public String bindEmail(@RequestBody BindRequest bindRequest,
                            OAuth2Authentication auth) {

        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        checkEmail(bindRequest);
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = RespHelper.or500(userService.findById(userId));
        //判断是否已经绑定
        if (!Strings.isNullOrEmpty(user.getEmail())) {
            throw new JsonResponseException(400, "user.email.already.bind");
        }

        User user1 = RespHelper.or500(userService.findByEmail(bindRequest.getIdentity()));
        if (Arguments.notNull(user1)) {
            throw new JsonResponseException(400, "user.email.already.other.bind");
        }
        //校验验证码是否正确
        if (!emailVerificationCodeService.verificationCode(
                VerificationCode.Type.BINGING, bindRequest.getIdentity(), bindRequest.getCode())) {
            throw new JsonResponseException(400, "user.code.is.error");
        }

        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setEmail(bindRequest.getIdentity());
        RespHelper.or500(userService.updateEmail(userUpdate));

        //删除验证码
        emailVerificationCodeService.invalidVerificationCode(VerificationCode.Type.BINGING, bindRequest.getIdentity());
        return SUCCESS;
    }

    /**
     * 解绑邮箱
     *
     * @param
     * @return
     */
    @PostMapping("/unbind/email")
    public String unBindEmail(@RequestBody BindRequest bindRequest,
                              OAuth2Authentication auth) {

        if (auth.isClientOnly()) {
            throw new JsonResponseException(401, "invalid_token");
        }
        checkEmail(bindRequest);
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        //校验验证码是否正确
        if (!emailVerificationCodeService.verificationCode(
                VerificationCode.Type.UN_BINGING, bindRequest.getIdentity(), bindRequest.getCode())) {
            throw new JsonResponseException(400, "user.code.is.error");
        }

        User userUpdate = new User();
        userUpdate.setId(userId);
        userUpdate.setEmail(null);
        RespHelper.or500(userService.updateEmail(userUpdate));

        //删除验证码
        emailVerificationCodeService.invalidVerificationCode(VerificationCode.Type.UN_BINGING, bindRequest.getIdentity());
        return SUCCESS;
    }

    private void checkPhone(BindRequest bindRequest) {
        if (Strings.isNullOrEmpty(bindRequest.getIdentity())) {
            throw new JsonResponseException(400, "user.phone.not.null");
        }
        if (!verifyMobile(bindRequest.getIdentity())) {
            throw new JsonResponseException(400, "user.phone.not.legal");
        }
        if (Strings.isNullOrEmpty(bindRequest.getCode())) {
            throw new JsonResponseException(400, "user.code.not.null");
        }
    }

    private void checkEmail(BindRequest bindRequest) {
        if (Strings.isNullOrEmpty(bindRequest.getIdentity())) {
            throw new JsonResponseException(400, "user.email.not.null");
        }
        if (!verifyEmail(bindRequest.getIdentity())) {
            throw new JsonResponseException(400, "user.email.not.legal");
        }
        if (Strings.isNullOrEmpty(bindRequest.getCode())) {
            throw new JsonResponseException(400, "user.code.not.null");
        }
    }


    @Data
    public static class BindRequest implements Serializable {
        private static final long serialVersionUID = 383085446640464169L;
        /**
         * 手机/邮箱
         */
        private String identity;

        /**
         * 验证码
         */
        private String code;
    }
}
