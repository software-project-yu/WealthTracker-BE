package com.WealthTracker.demo.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeminiRestTemplateConfig {


    @Bean
    @Qualifier("geminiRestTemplate")
    public RestTemplate geminiRestTemplate(){
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getInterceptors().add((request,body,execution)->execution.execute(request,body));
        return restTemplate;
    }

}
