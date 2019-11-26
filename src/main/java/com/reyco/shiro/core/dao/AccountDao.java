package com.reyco.shiro.core.dao;

import com.reyco.shiro.core.domain.AccountEntity;

public interface AccountDao extends BaseDao<AccountEntity>{
	
	public AccountEntity getByUsername(String username);
	
}
