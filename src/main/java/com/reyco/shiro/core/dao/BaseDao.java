package com.reyco.shiro.core.dao;

import java.util.List;
import java.util.Map;

import com.reyco.shiro.core.domain.BaseEntity;

public interface BaseDao<T extends BaseEntity> {
	
	T get(Integer id);
	
	Integer getByUsernameOrUnById(Map<String,Object> map);
	
	List<T> list(Map<String,Object> map);
	
	void update(BaseEntity baseEntity);
	
	void save(BaseEntity baseEntity);
	
	void delete(List<Integer> list);
}
