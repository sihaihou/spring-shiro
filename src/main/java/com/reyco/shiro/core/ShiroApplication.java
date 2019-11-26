package com.reyco.shiro.core;

import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.reyco.shiro.core.dao")
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class ShiroApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ShiroApplication.class, args);
	}
	/**
	 * 线程池
	 * @return
	 */
	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(1);
		return threadPoolTaskScheduler;
	}
	/**
	 * 解决cookie域名格式:
	 * 		An invalid domain [] was specified for this cookie问题解决方案
	 * @return
	 */
	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
		return (factory) -> factory.addContextCustomizers(
				(context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
	}
	
}
