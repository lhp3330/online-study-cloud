package com.xuecheng.base.exception;

/*
   @Class:ParamsIsNullException
   @Date:2024/1/11  22:52
*/

import com.xuecheng.base.enums.CustomException;

/**
 * 参数为空异常
 */
public class ParamsIsNullException extends BaseException {

    public ParamsIsNullException() {
    }

    public ParamsIsNullException(String errorMessage) {
        super(errorMessage);
    }

    public ParamsIsNullException(CustomException customException) {
        super(customException);
    }
}
