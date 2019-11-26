package com.reyco.shiro.core.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.shiro.core.utils.Result;

@RestController
@RequestMapping("/api")
public class RoleController {
	
	@GetMapping("/role/list")
	@RequiresPermissions("role:list")
	public Result rolelist() {
		return Result.success("role:list");
	}
	
	@PostMapping("/role/add")
	@RequiresPermissions("role:add")
	public Result add() {
		return Result.success("role:add");
	}
	
	@GetMapping("/role/delete")
	@RequiresPermissions("role:delete")
	public Result delete() {
		return Result.success("role:delete");
	}
	
	@PostMapping("/role/update")
	@RequiresPermissions("role:update")
	public Result update() {
		return Result.success("role:update");
	}
	
}
