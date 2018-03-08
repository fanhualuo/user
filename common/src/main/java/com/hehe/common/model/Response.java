package com.hehe.common.model;

import java.io.Serializable;

/**
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 4569349385519196012L;

    private boolean success;
    private T result;
    private String error;

    public Response() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.success = true;
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.success = false;
        this.error = error;
    }

    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response();
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> fail(String error) {
        Response<T> resp = new Response();
        resp.setError(error);
        return resp;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
