package com.hehe.common.util;

/**
 * 捕获异常类
 * @author xieqinghe .
 * @date 2017/10/17 下午4:12
 * @email qinghe101@qq.com
 */
public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = 657378777056762471L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
