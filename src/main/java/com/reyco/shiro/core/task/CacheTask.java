package com.reyco.shiro.core.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.reyco.shiro.core.cache.ConcurrentHashMapCache;


@Component
public class CacheTask {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Scheduled(cron = "0 * * * * ?")
	public void clear() {
		//logger.debug("缓存定时任务执行开始："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ConcurrentHashMapCache instance = ConcurrentHashMapCache.getInstance();
		instance.removeStrategy();
		//logger.debug("缓存定时任务执行结束："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	
	
	
}
