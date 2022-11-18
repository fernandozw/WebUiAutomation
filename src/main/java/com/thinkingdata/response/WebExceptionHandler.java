package com.thinkingdata.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/3/11 11:40
 */
@ControllerAdvice
@ResponseBody
public class WebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler
    public ResponseData catchException(Exception e) {
        log.error("【出现了异常】", e);
        return ResponseDataUtils.buildError();
    }
}
