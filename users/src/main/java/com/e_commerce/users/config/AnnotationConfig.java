package com.e_commerce.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.method.PrePostTemplateDefaults;

@Configuration
public class AnnotationConfig {

    @Bean
    static PrePostTemplateDefaults prePostTemplateDefaults() {
        return new PrePostTemplateDefaults();
    }
}
