package me.cu.week7.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import me.cu.week7.project.model.UserDto;
import me.cu.week7.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Создание нового пользователя")
    @PostMapping("signUp")
    public void AuthService(@Valid @RequestBody UserDto user, @RequestHeader("Basic") String basic) {
        authService.basicAuth(basic, AuthService.adminOnly);
        authService.signIn(user);
    }

}
