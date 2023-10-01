package com.project.TaskManger.category;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public void addCategory(@RequestBody CategoryDto request){
        categoryService.addCategory(request);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }
    @DeleteMapping("{category_id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void removeCategory(@PathVariable("category_id") int id){
        categoryService.removeCategory(id);
    }
    @PutMapping("{category_id}")
    @PreAuthorize("hasAuthority('admin:put')")
    public void updateCategory(@PathVariable("category_id") int id,
                               @RequestBody CategoryDto update){
        categoryService.updateCategory(id, update);
    }

}
