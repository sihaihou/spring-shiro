package com.reyco.shiro.core.service;

import java.util.List;
import java.util.Map;

import com.reyco.shiro.core.domain.BaseEntity;
import com.reyco.shiro.core.utils.Result;

public interface BaseService<T extends BaseEntity> {

	T get(Integer id);
	
	List<T> list(Map<String,Object> map); 
	
	Result update(T t);
	
	void delete(Integer id);
	
	Result deleteBatch(List<Integer> list);
	
	Result save(T t);
	
}
