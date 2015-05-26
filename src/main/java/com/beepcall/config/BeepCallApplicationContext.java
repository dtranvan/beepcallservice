package com.beepcall.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ComponentScan(basePackages = {"com.beepcall.controller",
        "com.beepcall.service", "com.beepcall.job"})
@PropertySource(value = "file:conf/application.properties", ignoreResourceNotFound = true)
@Import({PersistenceContext.class})
public class BeepCallApplicationContext {

}
