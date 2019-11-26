package com.reyco.shiro.core.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api")
public class IndexController {
	
	/**
	 * 返回主页  index.html
	 * @param session
	 * @return
	 */
	@GetMapping("/index")
	@RequiresPermissions("/")
	public ModelAndView index(HttpSession session) {
		return new ModelAndView("index");
	}
	
	
	
}
