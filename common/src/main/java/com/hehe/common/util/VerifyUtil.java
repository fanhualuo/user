package com.hehe.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式校验
 * @author xieqinghe .
 * @date 2018/1/16 下午3:43
 * @email qinghe101@qq.com
 */
public class VerifyUtil {

    private static final String EMAIL_REGULAR = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    private static final String PASSWORD_REGULAR = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[a-zA-Z0-9]{6,20}";

    private static final String MOBILE_REGULAR = "^1[^0,1,2\\D]\\d{9}$";

    /**
     * 校验邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean verifyEmail(String email) {
        boolean flag;
        try {
            Pattern regex = Pattern.compile(EMAIL_REGULAR);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;

    }

    /**
     * 手机号码格式检查
     *
     * @param mobile
     * @return
     */
    public static boolean verifyMobile(String mobile) {
        boolean flag;
        try {
            Pattern regex = Pattern.compile(MOBILE_REGULAR);
            Matcher matcher = regex.matcher(mobile);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 是否包含数字  大小写
     *
     * @param password
     * @return
     */
    public static boolean verifyPasswordFormat(String password) {
        boolean result;
        try {
            Pattern regex = Pattern.compile(PASSWORD_REGULAR);
            Matcher matcher = regex.matcher(password);
            result = matcher.matches();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
