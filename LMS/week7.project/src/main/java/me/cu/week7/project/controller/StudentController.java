package me.cu.week7.project.controller;

import me.cu.week7.project.enums.Role;
import me.cu.week7.project.model.UserDto;
import me.cu.week7.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.cu.week7.project.model.StudentDto;
import me.cu.week7.project.service.StudentService;

import java.util.UUID;

import static me.cu.week7.project.service.AuthService.adminAndMentor;

@RestController
@Tag(name = "Students", description = "Students API")
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final AuthService authService;



    @Autowired
    public StudentController(StudentService studentService, AuthService authService) {
        this.studentService = studentService;
        this.authService = authService;
    }

    @Operation(summary = "Получения студента по id")
    @GetMapping("/{id}")
    public StudentDto get(@PathVariable long id, @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        return studentService.get(id);
    }

    @Operation(summary = "Создание нового студента")
    @PostMapping
    public void add(@Valid @RequestBody StudentDto student, @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        studentService.add(student);
        UserDto studentUser = new UserDto();
        studentUser.setLogin(student.getLogin());
        studentUser.setPassword(UUID.randomUUID().toString());
        studentUser.setRole(Role.STUDENT);
        authService.signIn(studentUser);
    }

    @Operation(summary = "Удаление студента")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id, @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        studentService.delete(id);
    }

    @Operation(summary =  "Обновление студента")
    @PatchMapping
    public void patch(@PathVariable long id,  @RequestBody StudentDto student, @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, adminAndMentor);
        studentService.update(id, student);
    }
}
