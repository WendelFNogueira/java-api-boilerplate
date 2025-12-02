package br.com.wendelnogueira.javaapiboilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "java-api-boilerplate", version = "1.0.0", description = "A boilerplate API with basic CRUD operations.")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class JavaApiBoilerplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaApiBoilerplateApplication.class, args);
	}

}
