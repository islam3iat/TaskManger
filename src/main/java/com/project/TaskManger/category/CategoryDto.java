package com.project.TaskManger.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
public class CategoryDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    String title;
    @NotNull
    @NotEmpty
    @NotBlank
    String description;
}