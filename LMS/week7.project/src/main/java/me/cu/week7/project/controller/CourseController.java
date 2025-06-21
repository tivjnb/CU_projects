package me.cu.week7.project.controller;

import java.util.List;

import me.cu.week7.project.model.TopicDto;
import me.cu.week7.project.service.AuthService;
import me.cu.week7.project.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.cu.week7.project.model.CourseDto;
import me.cu.week7.project.service.CourseService;

import static me.cu.week7.project.service.AuthService.adminAndMentor;
import static me.cu.week7.project.service.AuthService.allUsers;

@RestController
@RequestMapping("/courses")
@Tag(name = "Courses", description = "Operations related to courses")
public class CourseController {

    private final CourseService courseService;
    private final TopicService topicService;
    private final AuthService authService;

    @Autowired
    public CourseController(CourseService courseService, TopicService topicService, AuthService authService) {
        this.courseService = courseService;
        this.topicService = topicService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto, @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        CourseDto createdCourse = courseService.createCourse(courseDto);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a course by ID")
    public ResponseEntity<CourseDto> getCourse(
            @PathVariable Long id,
            @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, allUsers);
        CourseDto course = courseService.getCourse(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all courses")
    public ResponseEntity<List<CourseDto>> getAllCourses(@RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, allUsers);
        List<CourseDto> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course by ID")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id,
            @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<Void> enrollStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        courseService.enrollStudent(courseId, studentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{courseId}/unenroll/{studentId}")
    @Operation(summary = "Unenroll a student from a course")
    public ResponseEntity<Void> unenrollStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        courseService.unenrollStudent(courseId, studentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{courseId}/topics")
    @Operation(summary = "Add topic for cource")
    public ResponseEntity<Void> addTopic(
            @PathVariable Long courseId,
            @Valid @RequestBody TopicDto topic,
            @RequestHeader("Basic") String basic
    ) {
        authService.basicAuth(basic, adminAndMentor);
        CourseDto course = courseService.getCourse(courseId);
        if (course != null) {
            TopicDto dbTopic = topicService.getTopic(topic.getId());
            courseService.AddTopic(courseId, dbTopic);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}