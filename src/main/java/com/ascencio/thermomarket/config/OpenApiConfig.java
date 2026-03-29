package com.ascencio.thermomarket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI thermoMarketOpenApi() {
        return new OpenAPI().info(new Info()
                .title("ThermoMarket API")
                .description("Spring Boot e-commerce backend for products, customers, orders, inventory, and shipping.")
                .version("v1")
                .contact(new Contact().name("Ascencio"))
                .license(new License().name("Private portfolio project")));
    }
}
