package com.hehe.user.util;

import com.google.common.base.Strings;
import com.hehe.common.util.CastUtil;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author xieqinghe .
 * @date 2018/3/13 上午11:44
 * @email qinghe101@qq.com
 */
public class AuthenticationUtil {


    /**
     * 根据个人级token获得用户id
     */
    public static Long getUserId(OAuth2Authentication auth) {
        if (auth == null || auth.isClientOnly()) {
            throw new InvalidTokenException("invalid_token");
        }
        String userId = auth.getUserAuthentication().getName();
        if (Strings.isNullOrEmpty(userId) || !userId.matches("\\d+")) {
            throw new InvalidTokenException("invalid_token");
        }
        return CastUtil.castLong(userId);
    }


}
