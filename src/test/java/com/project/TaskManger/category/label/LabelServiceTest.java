package com.project.TaskManger.category.label;

import com.github.javafaker.Faker;
import com.project.TaskManger.category.Category;
import com.project.TaskManger.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class LabelServiceTest {
    private LabelService underTest;
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private CategoryRepository categoryRepository;
    protected static final Faker FAKER=new Faker();

    @BeforeEach
    void setUp() {
        underTest=new LabelService(labelRepository,categoryRepository);
    }
    @Test
    void addLabel() {
        // Given
        String name=FAKER.name().title();
        String categoryName=FAKER.name().bloodGroup();
        LabelDto label=new LabelDto(name,categoryName);
        Category category=new Category(categoryName,FAKER.gameOfThrones().quote());
        when(labelRepository.existsLabelByNameAndCategoryByTitle(name,categoryName)).thenReturn(true);
        when(categoryRepository.findCategoryByTitle(categoryName)).thenReturn(Optional.of(category));
        //When
        underTest.addLabel(label);
        //Then
        ArgumentCaptor<Label> argumentCaptor=ArgumentCaptor.forClass(Label.class);
        verify(labelRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo(label.getName());
        assertThat(argumentCaptor.getValue().getCategory().getTitle()).isEqualTo(label.getCategoryName());
    }

    @Test
    void getLabels() {
        // Given
        int id=10;
        //When
        underTest.getLabels(id);
        //Then
        verify(labelRepository).findAllByCategory_Id(id);
    }
    @Test
    void getLabel() {
        // Given
        int id=10;
        Label label =new Label(FAKER.name().title(),new Category(FAKER.name().title(),FAKER.gameOfThrones().quote()));
        //When
        when(labelRepository.findLabelById(id)).thenReturn(Optional.of(label));
        Label expected = underTest.getLabel(id);
        //Then
        assertThat(expected).isEqualToIgnoringGivenFields(label,"id");
    }

    @Test
    void getLabelByName() {
        // Given
        String name=FAKER.name().title();
        Label expected=new Label(name,new Category(FAKER.name().title(),FAKER.gameOfThrones().quote()));
        when(labelRepository.findLabelByName(name)).thenReturn(Optional.of(expected));
        //When
        Label actual = underTest.getLabelByName(name);
        //Then
        assertThat(actual).isEqualToIgnoringGivenFields(expected,"id");

    }

    @Test
    void removeLabel() {
        // Given
        int id=10;
        when(labelRepository.existsLabelById(id)).thenReturn(true);
        //When
        underTest.removeLabel(id);
        //Then
        verify(labelRepository).deleteById(id);

    }

    @Test
    void updateLabel() {
        // Given
        int id=10;
        String name = FAKER.name().title();
        Category category = new Category(FAKER.name().bloodGroup()
                , FAKER.gameOfThrones().quote());
        Label label=new Label(name, category);

        when(labelRepository.findLabelById(id)).thenReturn(Optional.of(label));
        LabelDto updateRequest=new LabelDto(FAKER.name().nameWithMiddle(),category.getTitle());
        when(labelRepository.existsLabelByNameAndCategoryByTitle(updateRequest.getName(),updateRequest.getCategoryName())).thenReturn(true);
        when(categoryRepository.findCategoryByTitle(updateRequest.getCategoryName())).thenReturn(Optional.of(category));
        //When
        underTest.updateLabel(id,updateRequest);
        //Then
        ArgumentCaptor<Label> argumentCaptor=ArgumentCaptor.forClass(Label.class);
        verify(labelRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo(updateRequest.getName());
        assertThat(argumentCaptor.getValue().getCategory()).isEqualTo(label.getCategory());

    }
}