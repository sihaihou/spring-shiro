package com.reyco.shiro.core.service;

import java.util.List;

import com.reyco.shiro.core.domain.Permission;

public interface PermissionService {
	/**
	 * 根据用户id获取用户权限
	 * @param userId
	 * @return
	 */
	public List<Permission> getByUserId(Integer userId);
	
}
