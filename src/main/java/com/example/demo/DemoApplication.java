package com.example.demo;

import feign.Retryer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

import static feign.Retryer.NEVER_RETRY;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("examples");
	}

	@Bean
	public Retryer retryer() {
		return NEVER_RETRY;
	}

	@Configuration
	public static class CacheGetErrorHandlerConfigurer extends CachingConfigurerSupport {
		@Bean
		@Override
		public CacheErrorHandler errorHandler() {
			return new CacheGetErrorHandler();
		}

		public static class CacheGetErrorHandler extends SimpleCacheErrorHandler {
			public final AtomicInteger executionCounter = new AtomicInteger(0);

			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
				executionCounter.incrementAndGet();
			}
		}
	}
}
