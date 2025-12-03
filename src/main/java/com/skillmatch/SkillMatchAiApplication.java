package com.skillmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class SkillMatchAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillMatchAiApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SkillMatch AI API")
                        .version("1.0.0")
                        .description("API RESTful para Plataforma de Reskilling e Match de Vagas"));
    }
}
