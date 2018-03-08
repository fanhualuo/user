package com.hehe.common.util;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Collection;

/**
 * 判断工具类
 *
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
public class Arguments {
    public Arguments() {
    }

    public static <T extends Collection> boolean isNullOrEmpty(T t) {
        return isNull(t) || isEmpty(t);
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean notNull(Object o) {
        return o != null;
    }

    public static boolean isEmpty(String s) {
        return Strings.isNullOrEmpty(s);
    }

    public static <T extends Collection> boolean isEmpty(T t) {
        return t.isEmpty();
    }

    public static boolean notEmpty(String s) {
        return !isEmpty(s);
    }

    public static <T extends Collection> boolean notEmpty(T l) {
        return notNull(l) && !l.isEmpty();
    }

    public static boolean positive(Number n) {
        return n.doubleValue() > 0.0D;
    }

    public static boolean isPositive(Number n) {
        return n != null && n.doubleValue() > 0.0D;
    }

    public static boolean negative(Number n) {
        return n.doubleValue() < 0.0D;
    }

    public static boolean isNegative(Number n) {
        return n != null && n.doubleValue() < 0.0D;
    }

    public static <T> boolean equalWith(T source, T target) {
        return Objects.equal(source, target);
    }

    public static boolean not(Boolean t) {
        Preconditions.checkArgument(notNull(t));
        return !t.booleanValue();
    }

    public static boolean isDecimal(String str) {
        char[] var1 = str.toCharArray();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            char c = var1[var3];
            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }

    public static boolean isNumberic(String str) {
        boolean dot = false;
        char[] var2 = str.toCharArray();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            if (c == '.' && !dot) {
                dot = true;
            } else {
                if (c == '.') {
                    return false;
                }

                if (c < '0' || c > '9') {
                    return false;
                }
            }
        }

        return true;
    }
}
