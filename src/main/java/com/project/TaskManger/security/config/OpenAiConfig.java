package com.project.TaskManger.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(
        contact = @Contact(
                name = "islam",
                email = "islam3iat@gmail.com",
                url = "https://github.com/islam3iat"
        ),
        description = "OpenApi documentation for spring TaskManager",
        title = "TaskManager App",
        version = "1.0",
        license = @License(
                name = "MIT License",
                url = "https://opensource.org/licenses/MIT"
        )
),
        servers = {@Server(
                description = "Current Environment",
                url = "${server.servlet.context-path:/}"
        )},
        security = {
        @SecurityRequirement(
                name = "bearerAuth"
        )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenAiConfig {
}
