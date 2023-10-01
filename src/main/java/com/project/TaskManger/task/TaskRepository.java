package com.project.TaskManger.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface TaskRepository extends JpaRepository<Task,Integer> {

    boolean existsByTitleAndUserId(String title,int id);
    List<Task> findAllByUserId(int id);


    @Query("select t from Task t where t.user.id = :id and t.title like concat(:title, '%')")
    List<Task> findAllByUserIdAndTitleStartsWith(@Param("id") int id, @Param("title") String title);


    @Transactional
    @Modifying
    @Query("delete from Task t where t.id = ?1 and t.user.id = ?2")
    void deleteTaskByIdAndUserId(int id, int userId);
    @Query("select (count(t) > 0) from Task t where t.id = ?1 and t.user.id = ?2")
    Boolean existsTasksByIdAndUserId(int id, int userId);
    @Query("select (count(t) > 0) from Task t where t.id = ?1")
    boolean existsTasksById(int id);
    @Query("select t from Task t where t.user.id = ?1 and t.title = ?2")
    Optional<Task> findTaskByUserIdAndTitle(int id, String title);


}
