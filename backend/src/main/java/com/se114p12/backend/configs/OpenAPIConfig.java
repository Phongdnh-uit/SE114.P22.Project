package com.se114p12.backend.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    Server server = new Server();
    server.setUrl("https://api.se114.phongdnh.software");
    return new OpenAPI()
        .info(createInfo())
        .addServersItem(server)
        .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
        .components(
            new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
  }
}
