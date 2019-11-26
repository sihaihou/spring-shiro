package com.reyco.shiro.core.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.reyco.shiro.core.dao.AccountDao;
import com.reyco.shiro.core.domain.AccountEntity;
import com.reyco.shiro.core.service.AccountService;
import com.reyco.shiro.core.utils.Result;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	@Override
	public AccountEntity getByUsername(String username) {
		return accountDao.getByUsername(username);
	}

	@Override
	public AccountEntity get(Integer id) {
		return accountDao.get(id);
	}

	@Override
	public List<AccountEntity> list(Map<String, Object> map) {
		return null;
	}

	@Override
	public Result update(AccountEntity accountEntity) {
		if(null==accountEntity || null==accountEntity.getId()) {
			return Result.fail("参数异常");
		}
		String password = accountEntity.getPassword();
		if(!StringUtils.isBlank(password)) {
			AccountEntity selAccountEntity = accountDao.get(accountEntity.getId());
			String createPassword = createPassword(password, selAccountEntity.getSalt());
			accountEntity.setPassword(createPassword);
		}
		accountDao.update(accountEntity);
		return Result.success(accountEntity);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Result save(AccountEntity accountEntity) {
		String username = accountEntity.getUsername();
		String password = accountEntity.getPassword();
		// 1. 验证参数
		if(null==accountEntity || null==accountEntity.getRid() || null==username || null==password) {
			return Result.fail("参数异常");
		}
		// 2. 是否重复
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("username", username);
		Integer count = accountDao.getByUsernameOrUnById(map);
		if(count == 1) {
			return Result.fail("添加失败,该用户已存在...");
		}
		// 3. 构造对象
		// 3.1 设置盐值
		String salt = UUID.randomUUID().toString().replace("-", "");
		accountEntity.setSalt(salt);
		// 3.2 构建密码
		String createPassword = createPassword(password,salt);
		// 3.3  设置密码
		accountEntity.setPassword(createPassword);
		// 4. 新增
		accountDao.save(accountEntity);
		return Result.success(accountEntity);
	}
	@Override
	public void delete(Integer id) {
		List<Integer> list = Arrays.asList(id);
		deleteBatch(list);
	}
	@Override
	public Result deleteBatch(List<Integer> userIds) {
		if(null==userIds || userIds.isEmpty()) {
			return Result.fail("参数错误");
		}
		if(userIds.contains(1)) {
			return Result.fail("超级管理员无法删除...");
		}
		accountDao.delete(userIds);
		return Result.success("删除成功");
	}
	/**
	 * 根据明文密码和盐值生成加密密码
	 * @param password
	 * @param salt
	 * @return
	 */
	private static String createPassword(String password,String salt) {
		SimpleHash hash = new SimpleHash("md5", password, salt, 2);
		return hash.toString();
	}
}
