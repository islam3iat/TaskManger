package com.project.TaskManger.task;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Task}
 */
@Value
public class TaskWithScheduleDto implements Serializable {
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
    @NotNull
    LocalDateTime startDate;
    @NotNull
    @FutureOrPresent
    LocalDateTime dueDate;
}