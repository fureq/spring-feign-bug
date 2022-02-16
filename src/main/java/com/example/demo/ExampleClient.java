package com.example.demo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(value = "exampleclient", url = "https://exampleclient.com/")
public interface ExampleClient {
    @Cacheable(value = "examples", key = "#id")
    @RequestMapping(method = GET, value = "/examples/{id}", produces = "application/json")
    Example getById(@PathVariable("id") UUID id);
}
