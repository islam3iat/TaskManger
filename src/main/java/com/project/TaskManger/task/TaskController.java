package com.project.TaskManger.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @PostMapping
    public void addTask(@RequestBody TaskDto request, @RequestHeader("Authorization") String token){
        taskService.addTask(request, token);
    }
    @PostMapping("schedule")
    public void addTaskWithSchedule(
            @RequestBody TaskWithScheduleDto request,
            @RequestHeader(AUTHORIZATION) String token)
    {
    taskService.addTaskWithSchedule(request, token);
    }
    @GetMapping
    public List<Task> getTasks(@RequestHeader(AUTHORIZATION) String token){
        return taskService.getTasks(token);
    }
    @GetMapping("/{title}")
    public List<Task>  getTaskByTitleStartWith(@PathVariable("title") String title,
                                               @RequestHeader(AUTHORIZATION) String token){
        return taskService.getTaskByTitleStartWith(title, token);
    }
    @DeleteMapping("{task_id}")
    public void removeTask(@PathVariable("task_id") int id,
                           @RequestHeader(AUTHORIZATION) String token){
        taskService.removeTask(id, token);
    }
    @PutMapping("{task_id}")
    public void updateTask(@PathVariable("task_id") int id,
                           @RequestBody TaskDto update,
                           @RequestHeader(AUTHORIZATION) String token){
        taskService.updateTask(id, update, token);
    }
}
