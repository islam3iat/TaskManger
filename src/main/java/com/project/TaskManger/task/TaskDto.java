package com.project.TaskManger.task;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Task}
 */
@Value
public class TaskDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    String title;
    String description;
    @Enumerated(EnumType.STRING)
    Priority priority;
    @Enumerated(EnumType.STRING)
    Status status;
    String categoryTitle;


}