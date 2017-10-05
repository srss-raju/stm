package com.deloitte.smt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.deloitte.smt.util.SmtResponse;
 
@Configuration
public class SmtResponseConfiguration {
	
    @Bean
    public SmtResponse smtResponse(){
        return new SmtResponse();
    }
 
}