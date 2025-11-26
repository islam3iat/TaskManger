package com.project.TaskManager.task;

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