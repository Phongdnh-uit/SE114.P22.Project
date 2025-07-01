package com.se114p12.backend.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.se114p12.backend.constants.AppConstant.BACKEND_URL;
import static com.se114p12.backend.constants.AppConstant.DOMAIN;

@Configuration
public class OpenAPIConfig {
  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
  }

  private Info createInfo() {
    return new Info()
        .title("SE114.P12")
        .version("1.0")
        .description("API cho dự án nhập môn ứng dụng di dộng");
  }

  @Bean
  public OpenAPI myOpenAPI() {
    return new OpenAPI()
            .info(createInfo())
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(
                    new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
            .addServersItem(new io.swagger.v3.oas.models.servers.Server()
                    .url(DOMAIN));
  }
}
