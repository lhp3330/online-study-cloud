package com.xuecheng.base.exception;

/*
   @Class:DatabaseException
   @Date:2024/1/22  1:10
*/

import com.xuecheng.base.enums.CustomException;

public class DatabaseException extends BaseException {
    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(CustomException customException) {
        super(customException);
    }
}
