package com.project.TaskManger.task;

import com.github.javafaker.Faker;
import com.project.TaskManger.category.Category;
import com.project.TaskManger.category.CategoryRepository;
import com.project.TaskManger.security.config.JwtService;
import com.project.TaskManger.security.user.Role;
import com.project.TaskManger.security.user.User;
import com.project.TaskManger.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TaskIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private CategoryRepository categoryRepository;
    private final static String url="api/v1/task";
    @Test
    void canAddTask(){
        Faker faker=new Faker();
        String title=faker.name().title()
                , description=faker.gameOfThrones().character();
        TaskDto request=new TaskDto(title,description,null,null,title);
        User user=new User(faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                Role.USER);
        categoryRepository.save(new Category(title,description));
        String token = getToken(user);
        webTestClient.
                post().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(request), TaskDto.class).header("Authorization",token).exchange().expectStatus().isOk();
        List<TaskDtoMapper> taskList = webTestClient.
                get().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", token).
                exchange().
                expectStatus().
                isOk().
                expectBodyList(new ParameterizedTypeReference<TaskDtoMapper>() {
                }).returnResult().getResponseBody();
        Integer id = taskList.stream().filter(t -> t.getTitle().equals(title)).map(TaskDtoMapper::getId).findFirst().orElseThrow();

        TaskDtoMapper exepectedTask=new TaskDtoMapper(id,title,description,Priority.MEDIUM,Status.TO_DO);
        assertThat(taskList).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(exepectedTask);

        webTestClient.get()
                .uri(url+"/{title}",title)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<TaskDtoMapper>() {})
                .contains(exepectedTask);
    }
    @Test
    void canDeleteTask(){
        Faker faker=new Faker();
        String title=faker.name().title()
                , description=faker.gameOfThrones().quote();
        TaskDto request=new TaskDto(title,description,null,null,title);
        User user=new User(faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                Role.USER);
        String token = getToken(user);
        categoryRepository.save(new Category(title,description));
        webTestClient.
                post().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(request), TaskDto.class).header("Authorization",token).exchange().expectStatus().isOk();
        List<TaskDtoMapper> taskList = webTestClient.
                get().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", token).
                exchange().
                expectStatus().
                isOk().expectBodyList(new ParameterizedTypeReference<TaskDtoMapper>() {
                }).returnResult().getResponseBody();

        Integer id = taskList.stream().filter(t -> t.getTitle().equals(title)).map(TaskDtoMapper::getId).findFirst().orElseThrow();


        TaskDtoMapper exepectedTask=new TaskDtoMapper(id,title,description,Priority.MEDIUM,Status.TO_DO);
        assertThat(taskList).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(exepectedTask);
        webTestClient.delete()
                .uri(url+"/{id}",id).header(AUTHORIZATION,token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk();

        webTestClient.get().uri(url+"/{title}",title).header(AUTHORIZATION,token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk();

    }
    @Test
    void canUpdateTask(){
        Faker faker=new Faker();
        String title=faker.name().title()
                , description=faker.gameOfThrones().quote();
        TaskDto request=new TaskDto(title,description,null,null,title);
        User user=new User(faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                Role.USER);
        categoryRepository.save(new Category(title,description));
        String token = getToken(user);
        webTestClient.
                post().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(request), TaskDto.class).header("Authorization",token).exchange().expectStatus().isOk();
        List<TaskDtoMapper> taskList = webTestClient.
                get().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", token).
                exchange().
                expectStatus().
                isOk().expectBodyList(new ParameterizedTypeReference<TaskDtoMapper>() {
                }).returnResult().getResponseBody();

        Integer id = taskList.stream().filter(t -> t.getTitle().equals(title)).map(TaskDtoMapper::getId).findFirst().orElseThrow();


        TaskDtoMapper updateRequest=new TaskDtoMapper(id,title,description,Priority.MEDIUM,Status.TO_DO);
        assertThat(taskList).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(updateRequest);
        webTestClient.put()
                .uri(url+"/{id}",id).
                header(AUTHORIZATION,token).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).body(Mono.just(updateRequest), TaskDtoMapper.class).
                exchange().
                expectStatus().
                isOk();

        List<TaskDtoMapper> responseBody = webTestClient.get().
                uri(url + "/{title}", title).
                header(AUTHORIZATION, token).
                accept(MediaType.APPLICATION_JSON).
                exchange().
                expectStatus().
                isOk().
                expectBodyList(new ParameterizedTypeReference<TaskDtoMapper>() {
                }).returnResult().getResponseBody();

        assertThat(updateRequest).isEqualTo(responseBody.stream().findFirst().orElseThrow());

    }
    @NotNull
    private String getToken(User user1) {
        var user = User.builder()
                .firstname(user1.getFirstname())
                .lastname(user1.getLastname())
                .email(user1.getEmail())
                .password(user1.getPassword())
                .role(user1.getRole())
                .build();
        userRepository.save(user);
        var jwtToken = "Bearer " + jwtService.generateToken(user);
        return jwtToken;
    }
    private void createCategory(String title,String description){
        categoryRepository.save(new Category(title,description));

    }
}
