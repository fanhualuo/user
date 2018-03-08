package com.hehe.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 数据返回工具类
 *
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
@Data
@NoArgsConstructor
public class Paging<T> implements Serializable {
    private static final long serialVersionUID = 3385795752386139200L;
    private Long total;
    private List<T> data;

    public Paging(Long total, List<T> data) {
        this.data = data;
        this.total = total;
    }

    public Boolean isEmpty() {
        return Objects.equals(0L, this.total) || this.data == null || this.data.isEmpty();
    }

    public static <T> Paging<T> empty(Class<T> clazz) {
        List<T> emptyList = Collections.emptyList();
        return new Paging(0L, emptyList);
    }

    public static <T> Paging<T> empty() {
        return new Paging(0L, Collections.emptyList());
    }
}
