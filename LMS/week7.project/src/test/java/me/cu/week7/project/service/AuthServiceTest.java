package me.cu.week7.project.service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.enums.Role;
import me.cu.week7.project.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;
    private UserDto testUser;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        testUser = new UserDto();
        testUser.setLogin("testuser");
        testUser.setPassword("password");
        testUser.setRole(Role.STUDENT);
        authService.signIn(testUser);
    }

    @Test
    void signIn_ValidUser_AddsUserToSystem() {
        UserDto newUser = new UserDto();
        newUser.setLogin("newuser");
        newUser.setPassword("newpass");
        newUser.setRole(Role.MENTOR);

        authService.signIn(newUser);

        String credentials = Base64.getEncoder().encodeToString("newuser:newpass".getBytes());
        UserDto authenticatedUser = authService.auth(credentials);

        assertEquals(newUser.getLogin(), authenticatedUser.getLogin());
        assertEquals(newUser.getRole(), authenticatedUser.getRole());
    }

    @Test
    void auth_ValidCredentials_ReturnsUser() {
        String credentials = Base64.getEncoder().encodeToString("testuser:password".getBytes());

        UserDto authenticatedUser = authService.auth(credentials);

        assertEquals(testUser.getLogin(), authenticatedUser.getLogin());
        assertEquals(testUser.getRole(), authenticatedUser.getRole());
    }

    @Test
    void auth_InvalidFormat_ThrowsException() {
        String invalidCredentials = Base64.getEncoder().encodeToString("invalidformat".getBytes());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                authService.auth(invalidCredentials)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Invalid auth format"));
    }

    @Test
    void auth_WrongPassword_ThrowsException() {
        String credentials = Base64.getEncoder().encodeToString("testuser:wrongpassword".getBytes());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                authService.auth(credentials)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Invalid password"));
    }

    @Test
    void auth_NonExistingUser_ThrowsException() {
        String credentials = Base64.getEncoder().encodeToString("nonexistent:password".getBytes());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                authService.auth(credentials)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void basicAuth_ValidCredentialsAndRole_Succeeds() {
        String credentials = Base64.getEncoder().encodeToString("testuser:password".getBytes());

        assertDoesNotThrow(() ->
                authService.basicAuth(credentials, AuthService.allUsers)
        );
    }

    @Test
    void basicAuth_EmptyCredentials_ThrowsException() {
        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                authService.basicAuth("", AuthService.allUsers)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Empty basic header"));
    }

    @Test
    void basicAuth_UnauthorizedRole_ThrowsException() {
        String credentials = Base64.getEncoder().encodeToString("testuser:password".getBytes());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () ->
                authService.basicAuth(credentials, AuthService.adminOnly)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Unresolved operation"));
    }
}