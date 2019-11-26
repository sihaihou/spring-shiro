package com.reyco.shiro.core.dao;

import java.util.List;

import com.reyco.shiro.core.domain.Permission;

public interface PermissionDao {
	/**
	 * 根据用户id获取用户权限
	 * @param userId
	 * @return
	 */
	public List<Permission> getByUserId(Integer userId);
	
}
