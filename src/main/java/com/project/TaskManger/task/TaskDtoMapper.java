package com.project.TaskManger.task;

import com.project.TaskManger.security.user.UserDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Task}
 */
@Value
public class TaskDtoMapper implements Serializable {
    int id;
    String title;
    String description;
    Priority priority;
    Status status;



}