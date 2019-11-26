package com.reyco.shiro.core.exception;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reyco.shiro.core.utils.Result;
/**
 * 全局异常处理类
 * @author reyco
 *
 */
@SuppressWarnings("all")
@ControllerAdvice
public class GlobalDefultExceptionHandler {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ExceptionStrategyContext exceptionStrategyContext;
	/**
	 * 声明要捕获的异常
	 * 
	 * @param reuqest
	 * @param response
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result defultExcepitonHandler(HttpServletRequest reuqest, HttpServletResponse response, Exception ex) {
		ex.printStackTrace();
		try {
			// 自定义异常...
			if(ex instanceof MyException) {
				MyException myException = (MyException)ex; 
				return exceptionStrategyContext.getExceptionMsg(myException.getType(), ex);
			}
			// 未登录异常               
			if(ex instanceof UnauthenticatedException) {
				return getUnauthenticatedException(ex);
			}
			// 权限不足异常               
			if(ex instanceof UnauthorizedException) {
				return getUnauthorizedException(ex);
			}
			// 数组溢出异常
			if(ex instanceof ArrayIndexOutOfBoundsException) {
				return getArrayIndexOutOfBoundsException(ex);
			}
			// 数字转换异常
			if(ex instanceof NumberFormatException) {
				return getNumberFormatException(ex);
			}
			// 非法参数
			if(ex instanceof IllegalArgumentException) {
				return getIllegalArgumentException(ex);
			}
			// 空指针异常
			if(ex instanceof NullPointerException) {
				return getNullPointerException(ex);
			}
			// SQL语句异常
			if(ex instanceof SQLException) {
				return getSQLException(ex);
			}
			// IO输入输出异常
			if(ex instanceof IOException) {
				return getIOException(ex);
			}
			return getException(ex);
		} catch (Exception e) {
			return getException(e);
		}
	}
	/**
	 * 未登录异常
	 * @param ex
	 * @return
	 */
	private Result getUnauthenticatedException(Exception ex) {
		logger.error("未登录异常：" + ex.getMessage());
		UnauthenticatedException unauthenticatedException = (UnauthenticatedException) ex;
		return Result.fail("未登录异常：");
	}
	/**
	 * 权限不足异常
	 * @param ex
	 * @return
	 */
	private Result getUnauthorizedException(Exception ex) {
		logger.error("权限不足异常：" + ex.getMessage());
		UnauthorizedException unauthorizedException = (UnauthorizedException) ex;
		return Result.fail("权限不足异常：");
	}
	/**
	 * 非法参数异常
	 * @param ex
	 * @return
	 */
	private Result getIllegalArgumentException(Exception ex) {
		logger.error("非法参数异常：" + ex.getMessage());
		IllegalArgumentException illegalArgumentException = (IllegalArgumentException) ex;
		return Result.fail("非法参数异常：");
	}
	/**
	 * 空指针异常
	 * @param ex
	 * @return
	 */
	private Result getNullPointerException(Exception ex) {
		logger.error("空指针异常：" + ex.getMessage());
		NullPointerException nullPointerException = (NullPointerException) ex;
		return Result.fail("空指针异常：");
	}
	/**
	 * SQL语句异常
	 * @param ex
	 * @return
	 */
	private Result getSQLException(Exception ex) {
		logger.error("SQL语句异常：" + ex.getMessage());
		SQLException qQLException = (SQLException) ex;
		return Result.fail("SQL语句异常：");
	}
	/**
	 * 数字转换异常
	 * @param ex
	 * @return
	 */
	private Result getNumberFormatException(Exception ex) {
		logger.error("数字转换异常：" + ex.getMessage());
		NumberFormatException numberFormatException = (NumberFormatException) ex;
		return Result.fail("数字转换异常：");
	}
	/**
	 * 数组溢出异常
	 * @param ex
	 * @return
	 */
	private Result getArrayIndexOutOfBoundsException(Exception ex) {
		logger.error("数组溢出异常：" + ex.getMessage());
		ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = (ArrayIndexOutOfBoundsException) ex;
		return Result.fail("数组溢出异常：");
	}
	/**
	 * IO输入输出异常
	 * @param ex
	 * @return
	 */
	private Result getIOException(Exception ex) {
		logger.error("IO输入输出异常：" + ex.getMessage());
		IOException iOException = (IOException) ex;
		return Result.fail("IO输入输出异常：");
	}
	/**
	 * 系统异常
	 * @param ex
	 * @return
	 */
	private Result getException(Exception ex) {
		logger.error("系统异常：" + ex);
		return Result.fail("系统异常：msg=" + ex.getMessage());
	}
	
}
