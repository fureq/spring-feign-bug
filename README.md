# spring-feign-bug
It looks like change https://github.com/spring-cloud/spring-cloud-openfeign/pull/608 makes that cache  error handler execute twice.
To reproduce found bug please make following steps:
1. Open `DemoApplicationTests` class
2. Run test `shouldBeOneCallToCacheErrorHandler`
3. Assertion of `assertEquals (1, ((CacheGetErrorHandler) cacheErrorHandler).executionCounter.get())` fails. Result of `(CacheGetErrorHandler) cacheErrorHandler).executionCounter.get()` should be 1, but  it is 2. Looking  at execution:
```   
-> CacheInterceptor
   -> CacheAspectSupport#execute:402 (findCachedItem)
      -> handleCacheGetError 
   -> CacheAspectSupport#execute:421 (invokeOperation)
      -> FeignCachingInvocationHandlerFactory#create:53 (cacheInterceptor.invoke)
         -> CacheAspectSupport#execute:402 (findCachedItem) 
            -> handleCacheGetError
```
