package com.project.TaskManger.category.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Label}
 */
@Value
public class LabelDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    String name;
    @NotNull
    @NotEmpty
    @NotBlank
    String categoryName;
}