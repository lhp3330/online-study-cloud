package com.xuecheng.base.response;

/*
   @Class:ExceptionResponse
   @Date:2024/1/11  22:59
   异常统一返回模型
*/

public class ExceptionResponse {

    private String errMessage;

    public ExceptionResponse(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
