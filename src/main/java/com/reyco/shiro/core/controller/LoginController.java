package com.reyco.shiro.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.reyco.shiro.core.utils.CacheUtils;
import com.reyco.shiro.core.utils.CookieUtil;
import com.reyco.shiro.core.utils.R;
import com.reyco.shiro.core.utils.Result;

/**
 * 系统登录
 * 
 * @author reyco
 *
 */
@Controller
public class LoginController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 访问toLogin,然后访问login
	 * 
	 * @return
	 */
	@GetMapping(value = { "/", "login", "/toLogin" })
	public ModelAndView toLogin() {
		return new ModelAndView("login");
	}

	/**
	 * 登录，验证不成功，则跳转到login.html页面;验证成功则跳转到配置的页面
	 * 
	 * @param req
	 * @return
	 */
	@ResponseBody
	@PostMapping("/api/login")
	public R login(HttpServletRequest request, String username, String password, String captcha,
			Boolean rememberMe) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(captcha)) {
			logger.info("参数错误");
			return R.fail(R.REQUEST_FAIL, R.PARAM_ERROR);
		}
		String captchaCookie = CookieUtil.getCookieByName(request, "shiro_captcha");
		if (StringUtils.isBlank(captchaCookie)) {
			logger.info("没有验证码");
			return R.fail("没有验证码");
		}
		Object selCaptcha = CacheUtils.get(captchaCookie);
		CacheUtils.remove(captchaCookie);
		if (null == selCaptcha) {
			logger.info("验证码失效");
			return R.fail("验证码失效...");
		}
		if (!captcha.equalsIgnoreCase(selCaptcha.toString())) {
			logger.info("验证码错误");
			return R.fail("验证码错误...");
		}
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
		try {
			subject.login(token);
			return R.success(subject.getPrincipal());
		} catch (UnknownAccountException uae) {
			return R.fail("未知账户");
		} catch (IncorrectCredentialsException ice) {
			return R.fail("用户名或密码错误");
		} catch (LockedAccountException lae) {
			return R.fail("账户已锁定");
		} catch (ExcessiveAttemptsException eae) {
			return R.fail("用户名或密码错误次数过多");
		} catch (AuthenticationException ae) {
			return R.fail("用户名或密码错误");
		}
	}
}
