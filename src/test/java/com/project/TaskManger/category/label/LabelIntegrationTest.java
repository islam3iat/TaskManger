package com.project.TaskManger.category.label;

import com.github.javafaker.Faker;
import com.project.TaskManger.category.Category;
import com.project.TaskManger.category.CategoryRepository;
import com.project.TaskManger.security.auth.RegisterRequest;
import com.project.TaskManger.security.user.Role;
import com.project.TaskManger.security.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpHeaders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LabelIntegrationTest {
    @Autowired
    private  WebTestClient webTestClient;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private String url="api/v1/labels";
    protected static final Faker FAKER=new Faker();
    @Test
    void canAddMethod() {
        String title = FAKER.name().title();
        String description=FAKER.gameOfThrones().quote();
        Category category=new Category(title,description);
        categoryRepository.save(category);
        int category_id = categoryRepository.findCategoryByTitle(title).get().getId();
        String name=FAKER.name().nameWithMiddle();
        LabelDto request=new LabelDto(name,title);
        RegisterRequest registerRequest=new RegisterRequest(FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.internet().password(),
                Role.ADMIN);
        String token = webTestClient.post().
                uri("api/v1/auth/register").
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(registerRequest), RegisterRequest.class).
                exchange().
                expectStatus().
                isOk().expectBody(new ParameterizedTypeReference<String>() {
                }).returnResult().getResponseBody().replace("{\"token\":\"","").replace("\"}","");

        webTestClient.post().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(request), LabelDto.class).header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk();
        List<Label> labelList = webTestClient.get().uri(url+"/{category_id}",category_id).
                accept(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk().
                expectBodyList(new ParameterizedTypeReference<Label>() {
                }).returnResult().getResponseBody();
        Integer id = labelList.stream().
                filter(l -> l.getName().equals(name)).
                map(Label::getId).
                findFirst().
                orElseThrow();
        webTestClient.get().
                uri(url+"/label/{id}",id).
                accept(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk().
                expectBody(new ParameterizedTypeReference<LabelDto>() {
        }).isEqualTo(new LabelDto(request.getName(),null));
    }
    @Test
    void canRemoveLabel() {
        String title = FAKER.name().title();
        String description=FAKER.gameOfThrones().quote();
        Category category=new Category(title,description);
        categoryRepository.save(category);
        int category_id = categoryRepository.findCategoryByTitle(title).get().getId();
        String name=FAKER.name().nameWithMiddle();
        LabelDto request=new LabelDto(name,title);
        RegisterRequest registerRequest=new RegisterRequest(FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.internet().password(),
                Role.ADMIN);
        String token = webTestClient.post().
                uri("api/v1/auth/register").
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(registerRequest), RegisterRequest.class).
                exchange().
                expectStatus().
                isOk().expectBody(new ParameterizedTypeReference<String>() {
                }).returnResult().getResponseBody().replace("{\"token\":\"","").replace("\"}","");

        webTestClient.post().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(request), LabelDto.class).header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk();
        List<Label> labelList = webTestClient.get().uri(url+"/{category_id}",category_id).
                accept(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk().
                expectBodyList(new ParameterizedTypeReference<Label>() {
                }).returnResult().getResponseBody();
        Integer id = labelList.stream().
                filter(l -> l.getName().equals(name)).
                map(Label::getId).
                findFirst().
                orElseThrow();
        webTestClient.delete().
                uri(url+"/{id}",id).
                accept(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk();
        webTestClient.get().
                uri(url+"/label/{id}",id).
                accept(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isNotFound();

    }
    @Test
    void canUpdateLabel() {
        String title = FAKER.name().title();
        String description=FAKER.gameOfThrones().quote();
        Category category=new Category(title,description);
        categoryRepository.save(category);
        int category_id = categoryRepository.findCategoryByTitle(title).get().getId();
        String name=FAKER.name().nameWithMiddle();
        LabelDto request=new LabelDto(name,title);
         String labelUpdate=FAKER.gameOfThrones().character();


        RegisterRequest registerRequest=new RegisterRequest(FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.internet().password(),
                Role.ADMIN);
        String token = webTestClient.post().
                uri("api/v1/auth/register").
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(registerRequest), RegisterRequest.class).
                exchange().
                expectStatus().
                isOk().expectBody(new ParameterizedTypeReference<String>() {
                }).returnResult().getResponseBody().replace("{\"token\":\"","").replace("\"}","");

        webTestClient.post().
                uri(url).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(Mono.just(request), LabelDto.class).header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk();

        List<Label> labelList = webTestClient.get().uri(url+"/{category_id}",category_id).
                accept(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk().
                expectBodyList(new ParameterizedTypeReference<Label>() {
                }).returnResult().getResponseBody();
        Integer id = labelList.stream().
                filter(l -> l.getName().equals(name)).
                map(Label::getId).
                findFirst().
                orElseThrow();
        LabelDto updateRequest=new LabelDto(labelUpdate,category.getTitle());

        webTestClient.put().
                uri(url+"/{id}",id).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                body(Mono.just(updateRequest), LabelDto.class).
                exchange().
                expectStatus().
                isOk();

        webTestClient.get().
                uri(url+"/label/{id}",id).
                accept(MediaType.APPLICATION_JSON).
                header(AUTHORIZATION,String.format("Bearer %s",token)).
                exchange().
                expectStatus().
                isOk().expectBody(new ParameterizedTypeReference<LabelDto>() {
                }).isEqualTo(new LabelDto(updateRequest.getName(),null));

    }
}
