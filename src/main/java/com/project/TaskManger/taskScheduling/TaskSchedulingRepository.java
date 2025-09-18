package com.project.TaskManger.taskScheduling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskSchedulingRepository extends JpaRepository<TaskScheduling, Integer> {

    @Query("select (count(t) > 0) from TaskScheduling t where t.id = ?1")
    boolean existsTaskSchedulingById(int id);

    @Query("select (count(t) > 0) from TaskScheduling t where t.task.id = ?1")
    boolean existsTaskSchedulingByTaskId(int id);
    @Query("select t from TaskScheduling t where t.task.id = ?1")
    List<TaskScheduling> findAllByTask_Id(int id);


}