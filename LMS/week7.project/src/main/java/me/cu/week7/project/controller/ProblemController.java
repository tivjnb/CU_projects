package me.cu.week7.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.model.ProblemDto;
import me.cu.week7.project.service.AuthService;
import me.cu.week7.project.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static me.cu.week7.project.service.AuthService.adminAndMentor;
import static me.cu.week7.project.service.AuthService.allUsers;


@RestController
@RequestMapping("/problems")
@Tag(name = "Problems", description = "Operations related to problems")
public class ProblemController {
    private final ProblemService problemService;
    private final AuthService authService;

    @Autowired
    public ProblemController(ProblemService problemService, AuthService authService) {
        this.problemService = problemService;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a problem by ID")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id
    , @RequestHeader("Basic") String basic) throws HttpStatusException {
        authService.basicAuth(basic, allUsers);
        ProblemDto problem = problemService.getProblem(id);
        return new ResponseEntity<>(problem, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a problem by ID")
    public ResponseEntity<Void> deleteProblem(
            @PathVariable Long id,
            @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        problemService.deleteProblem(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
