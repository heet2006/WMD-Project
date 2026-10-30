package com.edu.studentpredict;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Student Academic Performance Prediction API",
        version = "1.0.0",
        description = "Production-grade RESTful API combining Decision Tree ML, JWT Authentication, and Student CRUD operations aligned with UN SDG 4 (Quality Education).",
        contact = @Contact(
            name = "Academic ML Project Team",
            email = "support@campus.edu"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    )
)
public class StudentPerformanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPerformanceApplication.class, args);
    }
}
