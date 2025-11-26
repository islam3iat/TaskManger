package com.project.TaskManager;

import com.project.TaskManager.security.auth.AuthenticationService;
import com.project.TaskManager.security.user.Role;
import com.project.TaskManager.security.user.UserDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class  TaskMangerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskMangerApplication.class, args);
	}



    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service) {
        return args -> {
            var admin = UserDto.builder()
                    .firstname("Admin")
                    .lastname("Admin")
                    .email("admin@mail.com")
                    .password("password")
                    .role(Role.ADMIN)
                    .build();
            System.out.println("Admin token: " + service.register(admin).getToken());

        };

    }

}
