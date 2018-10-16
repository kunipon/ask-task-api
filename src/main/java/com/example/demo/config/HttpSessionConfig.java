package com.example.demo.config;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import com.fasterxml.jackson.databind.ObjectMapper;

// Spring-socialだと使えないっぽいのでコメントアウト
//@Configuration
public class HttpSessionConfig implements BeanClassLoaderAware {
	private ClassLoader classLoader;
	
	@Bean
	public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
}
