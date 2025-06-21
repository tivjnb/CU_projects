package me.cu.week7.project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import me.cu.week7.project.enums.Role;

@Getter
@Setter
public class UserDto {
    private long id;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotNull
    private Role role;
}
