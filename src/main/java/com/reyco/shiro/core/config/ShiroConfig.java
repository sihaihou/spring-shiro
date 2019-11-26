package com.reyco.shiro.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
	/**
	 * 1. 凭证匹配器
	 * 
	 * @return
	 */
	@Bean("credentialsMatcher")
	public HashedCredentialsMatcher getCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");
		hashedCredentialsMatcher.setHashIterations(2);
		return hashedCredentialsMatcher;
	}

	/**
	 * 2. 自定义realm
	 * 
	 * @return
	 */
	@Bean("shiroUserRealm")
	public ShiroUserRealm getShiroUserRealm() {
		ShiroUserRealm shiroUserRealm = new ShiroUserRealm();
		shiroUserRealm.setCredentialsMatcher(getCredentialsMatcher());
		return shiroUserRealm;
	}
	/**
	 * 3.1 设置记住我cookie
	 * @return
	 */
	@Bean
	public SimpleCookie getSimpleCookie() {
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setMaxAge(60*60*24*7);
		simpleCookie.setName("rememberMe");
		return simpleCookie;
	}
	/**
	 * 3.2   配置记住我
	 * @return
	 */
	@Bean
	public CookieRememberMeManager getCookieRememberMeManager(){
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(getSimpleCookie());
		return cookieRememberMeManager;
	}
	
	
	
	@Bean
	public Authorizer authorizer(ShiroUserRealm shiroUserRealm) {
		ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
		Collection<Realm> crealms = new ArrayList<>();
		crealms.add(shiroUserRealm);
		authorizer.setRealms(crealms);
		return authorizer;
	}
	@Bean
	public Authenticator authenticator(ShiroUserRealm shiroUserRealm) {
		ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
		Collection<Realm> crealms = new ArrayList<>();
		crealms.add(shiroUserRealm);
		authenticator.setRealms(crealms);
		return authenticator;
	}
	/**
	 * 4. securityManager管理器
	 * 
	 * @return
	 */
	@Bean("securityManager")
	public DefaultWebSecurityManager getSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		ShiroUserRealm shiroUserRealm = getShiroUserRealm();
		securityManager.setAuthenticator(authenticator(shiroUserRealm));
		securityManager.setAuthorizer(authorizer(shiroUserRealm));
		securityManager.setRealm(shiroUserRealm);
		securityManager.setRememberMeManager(getCookieRememberMeManager());
		return securityManager;
	}
	
	
	
	
	/**
	 * 5. shiroFilter
	 * @param securityManager
	 * @return
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/api/login");
		// 登录成功后要跳转的链接
		//shiroFilterFactoryBean.setSuccessUrl("/api/index");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/login.html");
		// 拦截器链
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("/toLogin", "anon");
		map.put("/api/login", "authc");
		map.put("/api/logout", "logout");
		map.put("/admin/**", "user");
		map.put("/user/**", "user");
		map.put("/js/**", "anon");
		map.put("/css/**", "anon");
		map.put("/images/**", "anon");
		map.put("/**", "anon");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
		
		return shiroFilterFactoryBean;
	}

	
	
	
	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 开启Shiro的注解
	 * 		(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 		配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 * 
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}
	/**
	 * 配置authc，不是必须配置，如果不配置，表单的用户名和密码必须是：username，password
	 * 
	 * @return
	 */
	@Bean("formAuthenticationFilter")
	public FormAuthenticationFilter getFormAuthenticationFilter() {
		FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
		formAuthenticationFilter.setUsernameParam("username");
		formAuthenticationFilter.setPasswordParam("password");
		return formAuthenticationFilter;
	}

}