
package com.hehe.common.util;

import com.google.common.base.Optional;
import com.hehe.common.model.Response;

import java.util.Collections;

/**
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
public final class RespHelper {

    public static <T> T or(Response<T> resp, T failValue) {
        return resp.isSuccess() ? resp.getResult() : failValue;
    }

    public static Boolean orFalse(Response<Boolean> resp) {
        return or(resp, Boolean.FALSE);
    }

    public static <T> T or500(Response<T> resp) {
        if (resp.isSuccess()) {
            return resp.getResult();
        }
        throw new JsonResponseException(500, resp.getError());
    }

    public static <T> T orServEx(Response<T> resp) {
        if (resp.isSuccess()) {
            return resp.getResult();
        }
        throw new ServiceException(resp.getError());
    }

    public static <T, D extends T> Response<T> ok(D data) {
        Response<T> resp = new Response<>();
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> unwrap(Response<Optional<T>> resp, String error) {
        if (resp.isSuccess()) {
            if (resp.getResult().isPresent()) {
                return Response.ok(resp.getResult().get());
            }
            return Response.fail(error);
        }
        return Response.fail(resp.getError());
    }

    public static final class Opt {

        public static <T> Response<T> unwrap(Response<Optional<T>> resp, String error) {
            if (resp.isSuccess()) {
                if (resp.getResult().isPresent()) {
                    return Response.ok(resp.getResult().get());
                }
                return Response.fail(error);
            }
            return Response.fail(resp.getError());
        }

        public static <T, D extends T> Response<Optional<T>> of(D data) {
            return Response.ok(Optional.<T>of(data));
        }

        public static <T> Response<Optional<T>> absent() {
            return Response.ok(Optional.<T>absent());
        }

        public static <T, D extends T> Response<Optional<T>> fromNullable(D data) {
            return Response.ok(Optional.<T>fromNullable(data));
        }
    }


    public static final class Map {

        public static <K, V> Response<java.util.Map<K, V>> empty() {
            return Response.ok(Collections.<K, V>emptyMap());
        }
    }
}

