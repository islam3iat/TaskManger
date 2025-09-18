package com.project.TaskManger.category.label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Integer> {

    @Query("select (count(l) > 0) from Label l where l.name = ?1")
    boolean existsLabelByName(String name);
    @Query("select (count(l) > 0) from Label l where l.id = ?1")
    boolean existsLabelById(int id);
    @Query("select l from Label l where l.name = ?1")
    Optional<Label> findLabelByName(String name);
    @Query("select l from Label l where l.id = ?1")
    Optional<Label> findLabelById(int id);
    @Query("select l from Label l where l.category.id = ?1")
    List<Label> findAllByCategory_Id(int id);
    @Query("select (count(l) > 0) from Label l join Category c on l.category.id=c.id where  l.name=?1 and c.title=?2")
    boolean existsLabelByNameAndCategoryByTitle(String name,String title);
}