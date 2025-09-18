package com.project.TaskManger.security.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.project.TaskManger.security.user.User}
 */
@Value
@AllArgsConstructor
public class UserDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
   final String firstname;
    @NotNull
    @NotEmpty
    @NotBlank
    final String lastname;
    @NotNull
    @NotEmpty
    @NotBlank
    @Email
    final String email;
    @NotNull
    @NotEmpty
    @NotBlank
   final String password;
    Role role;
}