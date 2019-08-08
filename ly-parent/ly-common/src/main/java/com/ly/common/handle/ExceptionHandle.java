package com.ly.common.handle;

import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import com.ly.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author 70719
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> ExceptionHandle(LyException e){
        LyExceptionEnum em = e.getLyExceptionEnum();
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(em));
    }
}
