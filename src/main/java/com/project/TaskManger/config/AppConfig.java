package com.project.TaskManger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Clock;

@Configuration
public class AppConfig {
    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    }

}
