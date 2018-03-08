package com.hehe.common.util;

/**
 * 类型转换工具类
 *
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
public class CastUtil {
    /**
     * 转为String
     *
     * @param obj
     * @return
     */
    public static String castString(Object obj) {
        return castString(obj, "");
    }

    /**
     * 转为String (提供默认值)
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String castString(Object obj, String defaultValue) {
        return obj == null ? defaultValue : String.valueOf(obj);
    }

    /**
     * 转为 int 型
     */
    public static int castInt(Object obj) {
        return CastUtil.castInt(obj, 0);
    }

    /**
     * 转为 int 型（提供默认值）
     */
    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (Arguments.notEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /**
     * 转为 double 型
     */
    public static Double castDouble(Object obj) {
        return CastUtil.castDouble(obj, 0.0);
    }

    /**
     * 转为 double 型（提供默认值）
     */
    public static Double castDouble(Object obj, Double defaultValue) {
        Double doubleValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (Arguments.notEmpty(strValue)) {
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转为 long 型
     */
    public static Long castLong(Object obj) {
        return CastUtil.castLong(obj, 0L);
    }

    /**
     * 转为 long 型（提供默认值）
     */
    public static Long castLong(Object obj, Long defaultValue) {
        long longValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (Arguments.notEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }
}
