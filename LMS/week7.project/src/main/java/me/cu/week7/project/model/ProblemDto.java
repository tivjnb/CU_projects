package me.cu.week7.project.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProblemDto {
    private long id;

    @NotEmpty(message = "title cannot be empty")
    @Size(max = 100)
    private String title;

    @Size(max = 3000)
    private String description;
}
