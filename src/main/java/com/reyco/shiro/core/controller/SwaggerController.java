package com.reyco.shiro.core.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.shiro.core.domain.AccountEntity;
import com.reyco.shiro.core.utils.Result;

@RestController
@RequestMapping("/api")
public class SwaggerController {
	final static List<AccountEntity> accounts = new ArrayList<AccountEntity>();

	@GetMapping("/user/list")
	@RequiresPermissions("user:list")
	public Result list(@RequestParam Map<String, String> map) {
		List<AccountEntity> selUsers = new ArrayList<AccountEntity>();
		if (!map.containsKey("pageNo")) {
			return Result.fail("pageNo not is null...");
		}
		String pageNoStr = map.get("pageNo");
		Integer pageNo = Integer.parseInt(pageNoStr);
		Integer pageSize = 0;
		if (!map.containsKey("pageSize")) {
			pageSize = 5;
		} else {
			String pageSizeStr = map.get("pageSize");
			pageSize = Integer.parseInt(pageSizeStr);
		}
		for (int i = (pageNo - 1) * pageSize; i < pageNo * pageSize; i++) {
			if (i < accounts.size()) {
				selUsers.add(accounts.get(i));
			}
		}
		Result result = Result.success(selUsers);
		return result;
	}

	@GetMapping("/user/info/{id}")
	@RequiresPermissions("user:info")
	public Result list(@PathVariable(name = "id") Integer id) {
		if (null == id) {
			return Result.fail("id not is null...");
		}
		if (id > accounts.size() - 1) {
			return Result.success(null);
		}
		AccountEntity accountEntity = accounts.get(id - 1);
		return Result.success(accountEntity);
	}

	@PostMapping("/user/save")
	@RequiresPermissions("user:add")
	public Result list(@RequestBody AccountEntity accountEntity) {
		if (null == accountEntity || null == accountEntity.getId() || StringUtils.isBlank(accountEntity.getUsername())) {
			return Result.fail("参数错误,请联系管理员...");
		}
		accounts.add(accountEntity);
		return Result.success();
	}

	@PostMapping("/user/update")
	@RequiresPermissions("user:update")
	public Result update(@RequestBody AccountEntity accountEntity) {
		if (null == accountEntity || null == accountEntity.getId() || StringUtils.isBlank(accountEntity.getUsername())) {
			return Result.fail("参数错误,请联系管理员...");
		}
		if (accountEntity.getId() - 1 > accounts.size()) {
			return Result.fail("用户不存在");
		}
		accounts.remove(accountEntity.getId() - 1);
		accounts.add(accountEntity);
		return Result.success();
	}

	@DeleteMapping("/user/delete/{id}")
	@RequiresPermissions("user:delete")
	public Result update(@PathVariable(name = "id") Integer id) {
		if (null == id) {
			return Result.fail("id not is null...");
		}
		if (id > accounts.size()) {
			return Result.success(null);
		}
		accounts.remove(id - 1);
		return Result.success();
	}

	@PostConstruct
	private static void getAccounts() {
		AccountEntity accountEntity = null;
		for (int i = 1; i < 11; i++) {
			String username = "user" + i;
			accountEntity = new AccountEntity();
			accountEntity.setId(i);
			accountEntity.setUsername(username);
			accountEntity.setPassword("123456");
			accountEntity.setGmtCreate(new Date());
			accounts.add(accountEntity);
		}
	}

}
