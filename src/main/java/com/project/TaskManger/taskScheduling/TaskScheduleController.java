package com.project.TaskManger.taskScheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks/{task_id}/scheduling")
@RequiredArgsConstructor
public class TaskScheduleController {
    private final TaskSchedulingService taskSchedulingService;
    @PostMapping
    public void addTaskSchedule(@PathVariable("task_id") int id,@RequestBody TaskSchedulingDto request ){
        taskSchedulingService.addTaskSchedule(id, request);
    }
    @GetMapping
    public List<TaskScheduling> getTaskSchedules(@PathVariable("task_id") int id){
        return taskSchedulingService.getTaskSchedules(id);
    }
    @GetMapping("{schedule_id}")
    public TaskScheduling getTaskSchedule(@PathVariable("schedule_id") int id){
        return taskSchedulingService.getTaskSchedule(id);
    }
    @DeleteMapping("{schedule_id}")
    public void removeTaskSchedule(@PathVariable("schedule_id") int id){
        taskSchedulingService.removeTaskSchedule(id);
    }
    @PutMapping("{schedule_id}")
    public void updateTaskSchedule(@PathVariable("schedule_id") int id,
                                   @RequestBody TaskSchedulingDto update)
    {
        taskSchedulingService.updateTaskSchedule(id, update);
    }


}
