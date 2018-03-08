package com.hehe.user.web.handler;

import com.google.common.collect.Maps;
import com.hehe.common.util.JsonResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Controller 控制器错误处理器
 * 处理Controller抛出的异常
 *
 * @author xieqinghe .
 * @date 2018/1/14 下午4:10
 * @email xieqinghe@terminus.io
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Controller抛出JsonResponseException异常在这里处理
     */
    @ExceptionHandler(JsonResponseException.class)
    public ResponseEntity<Object> handleJsonResponseException(JsonResponseException ex) {
        Map<String, Object> error = Maps.newHashMap();
        String message=ex.getMessage();
        try {
            message=messageSource.getMessage(message,null,null);
        }catch (Exception e){
        }
        error.put("error", message);
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.valueOf(ex.getStatus()));
    }
}
