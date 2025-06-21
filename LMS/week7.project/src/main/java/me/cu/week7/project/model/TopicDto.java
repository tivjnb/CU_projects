package me.cu.week7.project.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class TopicDto {
    private long id;

    @NotEmpty(message = "title cannot be empty")
    @Size(max = 100)
    private String title;

    @Size(max = 3000)
    private String text; // description
    private ArrayList<Long> problemsId;
}
