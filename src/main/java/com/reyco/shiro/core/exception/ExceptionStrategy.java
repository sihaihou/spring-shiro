package com.reyco.shiro.core.exception;

import com.reyco.shiro.core.utils.Result;

/**
 * 自定义异常处理顶级接口
 * @author reyco
 *
 */
public interface ExceptionStrategy{
	
	/**
	 * 获取异常信息
	 * @param ex  异常对象
	 * @return
	 */
	Result getExceptionMsg(Exception ex);
	
}
