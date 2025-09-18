package com.project.TaskManger;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class AbstractTestContainer {
        @Container
        protected static MySQLContainer<?> mySQLContainer=new MySQLContainer<>("mysql:latest").
                withDatabaseName("task_manger").
                withUsername("dark3").
                withPassword("0556559704Islam");
        @DynamicPropertySource
        private static void registerDataSourceProperties(DynamicPropertyRegistry register){
            register.add("spring.datasource.url",
                    mySQLContainer::getJdbcUrl);
            register.add("spring.datasource.username",
                    mySQLContainer::getUsername);
            register.add("spring.datasource.password",
                    mySQLContainer::getPassword);
        }
        private static DataSource getDataSource(){
            return  DataSourceBuilder.create().
                    driverClassName(mySQLContainer.getDriverClassName()).
                    url(mySQLContainer.getJdbcUrl()).
                    username(mySQLContainer.getUsername()).
                    password(mySQLContainer.getPassword()).
                    build();

        }

        protected static final Faker FAKER=new Faker();


    }
