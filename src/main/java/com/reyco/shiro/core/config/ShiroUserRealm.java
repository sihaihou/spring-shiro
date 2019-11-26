package com.reyco.shiro.core.config;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.shiro.core.domain.AccountEntity;
import com.reyco.shiro.core.domain.Permission;
import com.reyco.shiro.core.service.AccountService;
import com.reyco.shiro.core.service.PermissionService;

public class ShiroUserRealm extends AuthorizingRealm {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PermissionService permissionService;
	
	/**
	 * 验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		String username = token.getPrincipal().toString();
		logger.info("----------认证-----------------username="+username);
		AccountEntity accountEntity = accountService.getByUsername(username);
		//if(null ==accountEntity) {
		//	throw new UnknownAccountException("未知用户...");
		//}
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(accountEntity, accountEntity.getPassword(),ByteSource.Util.bytes(accountEntity.getSalt()),getName());
		return simpleAuthenticationInfo;
	}
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		AccountEntity accountEntity = (AccountEntity) principal.getPrimaryPrincipal();
		logger.info("----------凭证-----------------password="+accountEntity.getPassword());
		List<Permission> permissions = permissionService.getByUserId(accountEntity.getId());
		if(null == permissions || permissions.size()==0) {
			return null;
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Permission permission : permissions) {
			String percode = permission.getPercode();
			if(null!=percode) {
				info.addStringPermission(percode);
			}
		}
		return info;
	}

	/**
	 * 清理缓存
	 */
	protected void clearCache() {
		Subject subject = SecurityUtils.getSubject();
		super.clearCache(subject.getPrincipals());
	}
}
