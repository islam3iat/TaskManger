package com.project.TaskManger.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("select (count(c) > 0) from Category c where c.title = ?1")
    boolean existsCategoryByTitle(String title);
    @Query("select (count(c) > 0) from Category c where c.id = ?1")
    boolean existsCategoryById(int id);

    @Query("select c from Category c where c.id = ?1")
    Optional<Category> findCategoryById(int id);

    @Query("select c from Category c where c.title = ?1")
    Optional<Category> findCategoryByTitle(String title);




}
