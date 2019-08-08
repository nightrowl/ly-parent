package com.ly.common.vo;

import com.ly.common.enums.LyExceptionEnum;
import lombok.Data;

/**
 *
 */
@Data
public class ExceptionResult {

    private String message;
    private Integer code ;
    private Long timestamp;


    public ExceptionResult(LyExceptionEnum e){
        this.code = e.getCode();
        this.message = e.getMessage();
        this.timestamp = System.currentTimeMillis();
    }
}
