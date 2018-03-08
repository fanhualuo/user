package com.hehe.common.util;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
public class Params {
    /**
     * Instantiates a new params.
     */
    private Params() {
    }

    /**
     * 过滤Map中的NULL或""值.
     *
     * @param criteria the criteria
     * @return the map
     */
    public static Map<String, Object> filterNullOrEmpty(Map<String, Object> criteria) {
        return Maps.filterEntries(criteria, new Predicate<Map.Entry<String, Object>>() {
            /**
             *
             * @param entry
             */
            @Override
            public boolean apply(Map.Entry<String, Object> entry) {
                Object v = entry.getValue();
                if (v instanceof String) {
                    return !Strings.isNullOrEmpty((String) v);
                }
                return v != null;
            }
        });
    }

    /**
     * Trim to null.
     *
     * @param str the str
     * @return the string
     */
    public static String trimToNull(String str) {
        return str != null ? Strings.emptyToNull(str.replace('\u00A0', ' ').trim()) : null;
    }

    /**
     * Trim to null.
     *
     * @param obj the obj
     * @return the string
     */
    public static String trimToNull(Object obj) {
        return obj != null ? trimToNull(obj.toString()) : null;
    }
}
