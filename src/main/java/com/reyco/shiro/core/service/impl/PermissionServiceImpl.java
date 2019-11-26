package com.reyco.shiro.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.shiro.core.dao.PermissionDao;
import com.reyco.shiro.core.domain.Permission;
import com.reyco.shiro.core.service.PermissionService;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Override
	public List<Permission> getByUserId(Integer userId) {
		return permissionDao.getByUserId(userId);
	}

}
