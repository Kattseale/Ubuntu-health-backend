package com.ubuntuhealth.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ubuntuHealthAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Ubuntu Health API")
                        .description("Backend REST API for Ubuntu Health Clinic Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Ubuntu Health Team")
                                .email("support@ubuntuhealth.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation"));
    }
}