package com.example.demo;

import com.example.demo.DemoApplication.CacheGetErrorHandlerConfigurer.CacheGetErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = DemoApplication.class)
@SpringBootTest
class DemoApplicationTests {
	private static final UUID EXAMPLE_ID = randomUUID();

	@MockBean
	CacheManager cacheManager;

	@Autowired
	ExampleClient exampleClient;

	@Autowired
	CacheErrorHandler cacheErrorHandler;

	@BeforeEach
	void setup() {
		var cache = Mockito.mock(Cache.class);
		Mockito.when(cacheManager.getCache("examples")).thenReturn(cache);
		Mockito.when(cache.get(EXAMPLE_ID)).thenThrow(new RuntimeException());
	}

	@Test
//	This test fails, there are 2 calls
	void shouldBeOneCallToCacheErrorHandler() {
		assertThrows(RuntimeException.class, () -> exampleClient.getById(EXAMPLE_ID));
		assertEquals (1, ((CacheGetErrorHandler) cacheErrorHandler).executionCounter.get());
	}

}
