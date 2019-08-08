package com.ly.common.exception;

import com.ly.common.enums.LyExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LyException extends RuntimeException {

    public LyExceptionEnum lyExceptionEnum;

}
