package com.hehe.user.util;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

/**
 * 用户密码加密类
 * @author xieqinghe .
 * @date 2017/11/14 下午4:32
 * @email qinghe101@qq.com
 */
public class MyPasswordEncoder implements PasswordEncoder {

    private static final HashFunction SHA512 = Hashing.sha512();
    private static final Splitter SPLITTER = Splitter.on('@').trimResults();
    private static final Joiner JOINER = Joiner.on('@').skipNulls();
    private static final HashFunction MD5 = Hashing.md5();

    /**
     * 对密码进行加密
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    @Override
    public String encode(CharSequence rawPassword) {
        String salt = MD5.newHasher().putString(UUID.randomUUID().toString(), Charsets.UTF_8).putLong(System.currentTimeMillis()).hash()
                .toString().substring(0, 4);
        String realPassword = SHA512.hashString(rawPassword + salt, Charsets.UTF_8).toString().substring(0, 20);
        return JOINER.join(salt, realPassword);
    }

    /**
     * 密码匹配
     * @param rawPassword 明文密码
     * @return 匹配成功返回true, 反之false
     * @paramen codedPassword 加密后的密码
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            Iterable<String> parts = SPLITTER.split(encodedPassword);
            String salt = Iterables.get(parts, 0);
            String realPassword = Iterables.get(parts, 1);
            return Objects.equal(SHA512.hashString(rawPassword + salt, Charsets.UTF_8).toString().substring(0, 20), realPassword);
        } catch (Exception e) {
            return false;
        }
    }


}
