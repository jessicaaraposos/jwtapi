package com.github.jessicaraposo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "JWT Validation API", version = "1.0", description = "API para validação de tokens JWT"))
public class JwtApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(JwtApiApplication.class, args);
	}
}
