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
public class CourseDto {
    private Long id;
    @NotEmpty(message = "title cannot be empty")
    @Size(max = 100)
    private String title;
    @Size(max = 3000)
    private String description;
    private ArrayList<Long> topicsId;
    private ArrayList<Long> studentsId;
}
