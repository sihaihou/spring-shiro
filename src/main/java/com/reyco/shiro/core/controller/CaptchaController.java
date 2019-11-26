package com.reyco.shiro.core.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.reyco.shiro.core.domain.Captcha;
import com.reyco.shiro.core.utils.CacheUtils;
import com.reyco.shiro.core.utils.CaptchaUtils;
import com.reyco.shiro.core.utils.CloseUtils;
import com.reyco.shiro.core.utils.CookieUtil;
import com.reyco.shiro.core.utils.R;

/**
 * 验证码接口
 * 
 * @author Admin
 *
 */
@Controller
@RequestMapping("/api")
public class CaptchaController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DefaultKaptcha captchaProducer;

	/**
	 * 生成验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@GetMapping("/captcha")
	public R getCaptcha(HttpServletRequest request, HttpServletResponse response) {
		// 1.生成验证码
		logger.info("生成验证码");
		String code = CaptchaUtils.createCode();
		String key = UUID.randomUUID().toString().replace("-", "");
		// 2.验证码放入缓存
		CacheUtils.put(key, code, 60L);
		// 3.验证码设置cookie
		CookieUtil.setCookie(request, response, "shiro_captcha", key, -1);
		// 4.验证码返回给前端
		Captcha captcha = new Captcha(key, code);
		return R.success(captcha, "获取验证码成功");
	}

	/**
	 * 生成验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("captcha1")
	public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		ServletOutputStream out = null;
		try {
			String capText = captchaProducer.createText();
			String key = UUID.randomUUID().toString().replace("-", "");
			// 2.验证码放入缓存
			CacheUtils.put(key, capText, 60L);
			// 3.验证码设置cookie
			CookieUtil.setCookie(request, response, "shiro_captcha", key, -1);
			BufferedImage bi = captchaProducer.createImage(capText);
			out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseUtils.close(out);
		}
		return null;
	}

}
