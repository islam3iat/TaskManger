package com.project.TaskManger.category.label;

import com.github.javafaker.Faker;
import com.project.TaskManger.category.Category;
import com.project.TaskManger.category.CategoryRepository;
import com.project.TaskManger.exception.ApiRequestException;
import com.project.TaskManger.exception.DuplicateResourceException;
import com.project.TaskManger.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabelService {
    private final LabelRepository labelRepository;
    private final CategoryRepository categoryRepository;


    public void addLabel(LabelDto request){
        if(labelRepository.existsLabelByName(request.getName()))
            throw new DuplicateResourceException("label with name [%S] is already exist".formatted(request.getName()));
        String categoryName = request.getCategoryName();
        Category category = categoryRepository.
                findCategoryByTitle(categoryName).
                orElseThrow(() ->
                        new NotFoundException("label with [%s] not found".formatted(categoryName)));
        Label label=new Label();
        label.setName(request.getName());
        label.setCategory(category);
        labelRepository.save(label);
    }
    public List<Label> getLabels(int categoryId){
        return labelRepository.findAllByCategory_Id(categoryId);
    }

    public Label getLabel(int id){
        return labelRepository.
                findLabelById(id).
                orElseThrow(() -> {
                    NotFoundException notFoundException = new NotFoundException("label with [%s] not found".formatted(id));
                    log.error("error in getting label with id {}",id,notFoundException);
                    return notFoundException;
                        }
                );
    }
    public Label getLabelByName(String name){
        return labelRepository.findLabelByName(name).
                orElseThrow(() ->
                new ApiRequestException("no such label with name ".formatted(name))
                );
    }
    public void removeLabel(int id){
        if(!labelRepository.existsLabelById(id))
            throw new NotFoundException("resource with [] NOT FOUND".formatted(id));
        labelRepository.deleteById(id);
    }
    public void updateLabel(int id,LabelDto update){
        if(!categoryRepository.existsCategoryByTitle(update.getCategoryName()))
            throw new ApiRequestException("no category with this name [%s]".formatted(update.getCategoryName()));
        Label label=labelRepository.findLabelById(id).orElseThrow(() ->
                new NotFoundException("resource with [%s] NOT FOUND".formatted(id)));
        boolean changes=false;
        if(update.getName()!=null&&!update.getName().equals(label.getName())){
            label.setName(update.getName());
            changes=true;
        }
        if(update.getCategoryName()!=null&&!update.getCategoryName().equals(label.getCategory())){
            label.setCategory(categoryRepository.findCategoryByTitle(update.getCategoryName()).orElseThrow());
            changes=true;
        }
        if(changes)
            labelRepository.save(label);
    }
}
