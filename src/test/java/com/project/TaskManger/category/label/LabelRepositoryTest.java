package com.project.TaskManger.category.label;

import com.project.TaskManger.AbstractTestContainer;
import com.project.TaskManger.category.Category;
import com.project.TaskManger.category.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class LabelRepositoryTest extends AbstractTestContainer {
    @Autowired
    private LabelRepository underTest;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void existsLabelByNameAndCategoryByTitle() {
        // Given
        String name=FAKER.name().bloodGroup();
        String title=FAKER.name().title();
        Category category=new Category(title,FAKER.gameOfThrones().quote());
        Label label=new Label(name,category);
        categoryRepository.save(category);
        underTest.save(label);
        //When
        boolean actual = underTest.existsLabelByNameAndCategoryByTitle(name, title);
        //Then
        assertThat(actual).isTrue();

    }
}