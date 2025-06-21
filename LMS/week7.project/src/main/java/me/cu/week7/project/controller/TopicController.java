package me.cu.week7.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.cu.week7.project.model.ProblemDto;
import me.cu.week7.project.model.TopicDto;
import me.cu.week7.project.service.AuthService;
import me.cu.week7.project.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static me.cu.week7.project.service.AuthService.adminAndMentor;
import static me.cu.week7.project.service.AuthService.allUsers;


@RestController
@RequestMapping("/topics")
@Tag(name = "Topics", description = "Operations related to topics")
public class TopicController {
    private final TopicService topicService;
    private final AuthService authService;

    @Autowired
    public TopicController(TopicService topicService, AuthService authService) {
        this.topicService = topicService;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a topic by ID")
    public ResponseEntity<TopicDto> getTopic(
            @PathVariable long id,
            @RequestHeader("Basic") String basic
    ){
        authService.basicAuth(basic, allUsers);
        TopicDto topicDto = topicService.getTopic(id);
        return new ResponseEntity<>(topicDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a topic by ID")
    public ResponseEntity<Void> deleteTopic(
            @PathVariable Long id,
            @RequestHeader("Basic") String basic
    ) {
        authService.basicAuth(basic, adminAndMentor);
        topicService.deleteTopic(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/problems")
    @Operation(summary = "Add a problem to the topic")
    public ResponseEntity<Void> addProblem(@PathVariable long id, @RequestBody ProblemDto problemDto) {
        topicService.AddProblem(id, problemDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
