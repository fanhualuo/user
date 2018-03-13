package com.hehe.user.verification;

import com.google.common.base.Throwables;
import com.hehe.common.event.CoreEventDispatcher;
import com.hehe.common.model.Response;
import com.hehe.common.util.VerificationCodeUtil;
import com.hehe.common.util.VerifyUtil;
import com.hehe.user.verification.model.Email;
import com.hehe.user.verification.model.VerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 短信验证service服务
 *
 * @author xieqinghe .
 * @date 2018/3/11 下午7:42
 * @email xieqinghe@terminus.io
 */
@Slf4j
@Service
public class EmailVerificationCodeServiceImpl extends AbstractVerificationCodeService {


    @Autowired
    protected RedisTemplate<String, VerificationCode> redisTemplate;

    @Autowired
    protected CoreEventDispatcher coreEventDispatcher;

    public static final String EMAIL_CODE_PATH = "redis.verification.code.email:";

    @Value("${verification.code.limit.count:20}")
    public Integer EMAIL_CODE_LIMIT_COUNT;

    @Value("${verification.code.limit.failure_time:300}")
    public Integer EMAIL_CODE_LIMIT_FAILURE_TIME;

    @Value("${verification.email.username}")
    private String EMAIL_USERNAME;


    /**
     * 发送验证码
     *
     * @param sendTo 发送给
     * @param type   类型
     * @return 验证码
     */
    @Override
    public Response<String> sendVerificationCode(String sendTo, VerificationCode.Type type) {

        try {
            if (!VerifyUtil.verifyEmail(sendTo)) {
                return Response.fail("user.email.not.legal");
            }
            //获取随机验证码
            String code = VerificationCodeUtil.getCodeForNubmer(6);
            String path = getCodePath(type, sendTo);
            Integer count = 1;
            //获得今天发送验证码次数
            VerificationCode verificationCode1 = getVerificationCode(path);
            if (verificationCode1 != null) {
                count = verificationCode1.getCount() + 1;
            }
            if (!this.checkCodeLimitCount(count)) {
                //超过最大限制次数
                return Response.fail("send.email.code.exceed.limit");
            }
            VerificationCode verificationCode2 = new VerificationCode(sendTo, code, type.getValue(), count);
            //验证码存入redis(如果以前存在，则使用这条新的覆盖)
            this.putVerificationCode(verificationCode2);

            //发送验证码，使用接口
            sendEmail(sendTo, type, code);

            return Response.ok("success");

        } catch (Exception e) {
            log.error("send email code fail, sendTo:{}, cause:{}", sendTo, Throwables.getStackTraceAsString(e));
            return Response.fail("send.email.code.fail");
        }
    }


    /**
     * 异步发送邮件
     */
    private void sendEmail(String sendTo, VerificationCode.Type type, String code) {
        String content = "亲爱的用户：您好！感谢您使用服务，您正在进行邮箱验证，本次请求的" + type.getDisplay() + "VerificationCode为：" + code;
        Email email = new Email(EMAIL_USERNAME, sendTo, "账号安全验证", content);
        //发送验证码，使用接口
        this.coreEventDispatcher.publish(email);
    }

    @Override
    protected RedisTemplate<String, VerificationCode> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    protected String getCodePathRoot() {
        return EMAIL_CODE_PATH;
    }

    @Override
    protected Integer getCodeLimitCount() {
        return EMAIL_CODE_LIMIT_COUNT;
    }

    @Override
    protected Integer getCodeFailureTime() {
        return EMAIL_CODE_LIMIT_FAILURE_TIME;
    }


}
