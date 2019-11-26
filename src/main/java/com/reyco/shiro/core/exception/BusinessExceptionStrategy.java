package com.reyco.shiro.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.reyco.shiro.core.utils.Result;
/**
 * 自定义异常处理实现2：
 * 			业务异常：
 * 			Component("business")的value必须和MyException的属性type一致
 * @author reyco
 *
 */
@Component("business")
public class BusinessExceptionStrategy implements ExceptionStrategy {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Result getExceptionMsg(Exception ex) {
		BusinessException businessException = (BusinessException) ex;
		logger.error("业务异常：" + businessException.getMsg());
		return Result.error("业务异常,code=" + businessException.getCode() + ",msg=" + businessException.getMsg());
	}

}
