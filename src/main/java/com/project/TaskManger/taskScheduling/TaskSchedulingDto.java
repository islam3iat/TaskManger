package com.project.TaskManger.taskScheduling;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link TaskScheduling}
 */
@Value
public class TaskSchedulingDto implements Serializable {
    @NotNull
    LocalDateTime startDate;
    @NotNull
    @FutureOrPresent
    LocalDateTime dueDate;

}