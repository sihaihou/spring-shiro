package com.reyco.shiro.core.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.shiro.core.domain.AccountEntity;
import com.reyco.shiro.core.exception.ArgumentException;
import com.reyco.shiro.core.service.AccountService;
import com.reyco.shiro.core.utils.R;
import com.reyco.shiro.core.utils.Result;

@RestController
@RequestMapping("/api/account")
public class UserController {
	
	@Autowired
	private AccountService accountService;
	/**
	 * 添加用户
	 * @param accountEntity
	 * @return
	 * @throws ArgumentException
	 */
	@PostMapping("save")
	@RequiresPermissions("user:add")
	public Result save(@RequestBody AccountEntity accountEntity) throws ArgumentException {
		return accountService.save(accountEntity);
	}
	
	/**
	 * 删除用户
	 * @param accountEntity
	 * @return
	 * @throws ArgumentException
	 */
	@DeleteMapping("/delete")
	@RequiresPermissions("user:delete")
	public Result delete(@RequestParam("userIds") List<Integer> userIds) throws ArgumentException {
		return accountService.deleteBatch(userIds);
	}
	/**
	 * 删除用户
	 * @param accountEntity
	 * @return
	 * @throws ArgumentException
	 */
	@PostMapping("/update")
	@RequiresPermissions("user:update")
	public Result update(@RequestBody AccountEntity accountEntity) throws ArgumentException {
		return accountService.update(accountEntity);
	}
	@GetMapping("/getInfo")
	@RequiresPermissions("user:info")
	public String getInfo(Integer userId) {
		AccountEntity accountEntity = accountService.get(userId);
		if(null == accountEntity) {
			return R.noData().toJSON();
		}
		accountEntity.setGmtCreate(new Date());
		return R.success(accountEntity).toJSON();
	}
	
}
