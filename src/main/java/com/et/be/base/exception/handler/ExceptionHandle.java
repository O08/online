package com.et.be.base.exception.handler;

import com.et.be.base.exception.ErrorCodeException;
import com.et.be.base.vo.ResponseVO;
import com.et.be.online.enums.CodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理
 *
 * @author yanpanyi
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    public ResponseVO exceptionHandler(Exception e) {
        log.error("error:", e);
        return new ResponseVO(CodeEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ErrorCodeException.class)
    public ResponseVO errorCodeHandler(ErrorCodeException e) {
        return new ResponseVO(e.getCode());
    }

}
