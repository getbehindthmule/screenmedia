package com.greenhills.fuel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-config.yml")
public class AppConfig {
    // useful later if application becomes too complex for autowiring

}
