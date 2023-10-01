package com.project.TaskManger.category;

import com.project.TaskManger.exception.DuplicateResourceException;
import com.project.TaskManger.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public void addCategory(CategoryDto request){
        if(categoryRepository.existsCategoryByTitle(request.getTitle()))
            throw new DuplicateResourceException("the category with title [%s] already exist".
                    formatted(request.getTitle()));
        Category category=new Category();
        category.setTitle(request.getTitle());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
    }
    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }
    public Category getCategory(int id){
        return categoryRepository.findById(id).orElseThrow(() ->
               new NotFoundException("category with [%s] not found".formatted(id)));
    }
    public void removeCategory(int id){
        if(!categoryRepository.existsCategoryById(id))
            throw new NotFoundException("category with [%s] not found".formatted(id));
        categoryRepository.deleteById(id);
    }
    public void updateCategory(int id,CategoryDto update){
        if(categoryRepository.existsCategoryByTitle(update.getTitle()))
            throw new DuplicateResourceException("the category with title [%s] already exist".
                    formatted(update.getTitle()));
        Category category=categoryRepository.
                findCategoryById(id).orElseThrow(() ->
                        new NotFoundException("category with [%s] not found".formatted(id)));
        Boolean changes=false;
        if(update.getTitle()!=null&&!update.getTitle().equals(category.getTitle())){
            category.setTitle(update.getTitle());
            changes=true;
        }
        if(update.getDescription()!=null&&!update.getDescription().equals(category.getDescription())){
            category.setDescription(update.getDescription());
            changes=true;
        }
        if(changes){
            categoryRepository.save(category);
        }
    }
}
