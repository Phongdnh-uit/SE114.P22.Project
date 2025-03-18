package com.se114p12.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }

    private Contact createContact() {
        return new Contact()
                .email("dangnguyenhuyphong@gmail.com")
                .name("Codeaholic")
                .url("https://www.facebook.com/profile.php?id=100044071402752");
    }

    private License createLicense() {
        return new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");
    }

    private Info createInfo() {
        return new Info()
                .title("SE114.P12 - UIT - CODEAHOLIC")
                .version("1.0")
                .contact(createContact())
                .description("API cho dự án nhập môn ứng dụng di dộng")
                .license(createLicense());
    }

    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(createInfo())
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(
                        new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }
}
