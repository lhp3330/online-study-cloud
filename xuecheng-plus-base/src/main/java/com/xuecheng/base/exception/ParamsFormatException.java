package com.xuecheng.base.exception;

/*
   @Class:ParamsFormatException
   @Date:2024/1/11  22:53
*/

import com.xuecheng.base.enums.CustomException;

/**
 * 参数格式异常 (example： require a num but accepted a string)
 */
public class ParamsFormatException extends BaseException{

    public ParamsFormatException() {
    }

    public ParamsFormatException(String message) {
        super(message);
    }

    public ParamsFormatException(CustomException customException) {
        super(customException);
    }

}
