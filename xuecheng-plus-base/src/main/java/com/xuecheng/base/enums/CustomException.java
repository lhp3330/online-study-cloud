
package com.xuecheng.base.enums;


/**
 * 通用错误信息
 */
public enum CustomException {

	UNKOWN_ERROR("执行过程异常，请重试"),
	PARAMS_ERROR("非法参数"),
	OBJECT_NULL("对象为空"),
	QUERY_NULL("查询结果为空"),
	REQUEST_NULL("请求参数为空"),
	CANT_MOVE("当前位置已无法移动"),
	CANT_DELETE("当前课程无法删除");

	private final String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	CustomException(String errMessage) {
		this.errorMessage = errMessage;
	}

}

