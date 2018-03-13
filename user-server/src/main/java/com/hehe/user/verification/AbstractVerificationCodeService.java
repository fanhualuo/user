package com.hehe.user.verification;

import com.hehe.common.model.Response;
import com.hehe.user.verification.model.VerificationCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author xieqinghe .
 * @date 2018/3/11 下午7:45
 * @email xieqinghe@terminus.io
 */
public abstract class AbstractVerificationCodeService {

    /**
     * 获得redisTemplate
     * @param
     * @return
     */
    protected abstract RedisTemplate<String, VerificationCode> getRedisTemplate();

    /**
     * 获取redis存储根目录
     *
     * @param
     * @return
     */
    protected abstract String getCodePathRoot();

    /**
     * 获取验证码最大限制次数
     *
     * @param
     * @return
     */
    protected abstract Integer getCodeLimitCount();

    /**
     * 获取验证码失效时间,单位为秒、s
     *
     * @param
     * @return
     */
    protected abstract Integer getCodeFailureTime();

    /**
     * 发送验证码
     *
     * @param sendTo 发送给
     * @param type   类型
     * @return 验证码
     */
    public abstract Response<String> sendVerificationCode(String sendTo, VerificationCode.Type type);


    /**
     * 根据type和identity获得redis存储路径
     *
     * @param type     类型
     * @param identity 账户
     * @return
     */
    protected String getCodePath(VerificationCode.Type type, String identity) {
        return getCodePathRoot() + type.getValue() + "." + identity;
    }


    /**
     * 根据路径获取发送验证码内容
     *
     * @param path
     * @return
     */
    protected VerificationCode getVerificationCode(String path) {
        return getRedisTemplate().opsForValue().get(path);
    }


    /**
     * 验证码存入redis,设置失效时间
     *
     * @param verificationCode
     * @return
     */
    protected void putVerificationCode(VerificationCode verificationCode) {
        ValueOperations<String,VerificationCode> temp= getRedisTemplate().opsForValue();
        getRedisTemplate().opsForValue().set(getCodePath(VerificationCode.Type.from(verificationCode.getType()), verificationCode.getIdentity()), verificationCode, getCodeFailureTime(), TimeUnit.SECONDS);
    }

    /**
     * 验证码校验
     *
     * @param
     * @return
     */
    public Boolean verificationCode(VerificationCode.Type type, String identity, String code) {
        try {
            VerificationCode verificationCode = getVerificationCode(getCodePath(type, identity));
            if (verificationCode.getCode().equals(code)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }


    /**
     * 是否操作最大限制次数，超过返回false
     *
     * @param
     * @return
     */
    protected Boolean checkCodeLimitCount(Integer count) {
        try {
            if (getCodeLimitCount() >= count) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}