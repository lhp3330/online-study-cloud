package com.xuecheng.base.exception;

/*
   @Class:BaseException
   @Date:2024/1/11  21:34
*/

import com.xuecheng.base.enums.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(CustomException customException) {
        super(customException.getErrorMessage());
    }


}
