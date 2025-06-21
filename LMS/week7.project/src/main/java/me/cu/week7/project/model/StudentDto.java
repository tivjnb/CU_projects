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
public class StudentDto {
    private long id;

    @NotEmpty(message = "login cant be empty")
    @Size(max = 30)
    private String login;

    @NotEmpty(message = "firstName cannot be empty")
    @Size(max = 100)
    private String firstName;

    @NotEmpty(message = "lastName cannot be empty")
    @Size(max = 100)
    private String lastName;

    private String phoneNumber;
    private ArrayList<Long> problemsId;

    public void solveProblem(long id) {
        problemsId.add(id);
    }

}
