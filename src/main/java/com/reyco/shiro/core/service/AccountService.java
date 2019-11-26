package com.reyco.shiro.core.service;

import com.reyco.shiro.core.domain.AccountEntity;

public interface AccountService extends BaseService<AccountEntity>{
	
	public AccountEntity getByUsername(String username);
	
}
