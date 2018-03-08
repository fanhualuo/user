package com.hehe.common.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 分页页数工具类
 *
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
@Data
@NoArgsConstructor
public class PageInfo {
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";
    private Integer offset;
    private Integer limit;


    public static PageInfo of(Integer pageNo, Integer size) {
        return new PageInfo(pageNo, size);
    }

    public PageInfo(Integer pageNo, Integer size) {
        pageNo = (Integer) MoreObjects.firstNonNull(pageNo, Integer.valueOf(1));
        size = (Integer) MoreObjects.firstNonNull(size, Integer.valueOf(20));
        this.limit = size.intValue() > 0 ? size.intValue() : 20;
        this.offset = (pageNo.intValue() - 1) * size.intValue();
        this.offset = this.offset.intValue() > 0 ? this.offset.intValue() : 0;
    }

    public Map<String, Object> toMap() {
        return this.toMap((String) null, (String) null);
    }

    public Map<String, Object> toMap(String key1, String key2) {
        Map param = Maps.newHashMapWithExpectedSize(2);
        param.put(Strings.isNullOrEmpty(key1) ? "offset" : key1, this.offset);
        param.put(Strings.isNullOrEmpty(key2) ? "limit" : key2, this.limit);
        return param;
    }
}
