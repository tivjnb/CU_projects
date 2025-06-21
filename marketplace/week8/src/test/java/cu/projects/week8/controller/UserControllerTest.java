package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.UserDto;
import cu.projects.week8.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        UserDto user = new UserDto();
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.add(user)).thenReturn(1L);

        Long result = userController.create(user);

        assertEquals(1L, result);
        verify(userService, times(1)).add(user);
    }

    @Test
    public void testGetById() {
        long id = 1L;
        UserDto user = new UserDto();
        user.setId(id);
        user.setName("Test User");
        user.setEmail("test@example.com");

        when(userService.getById(id)).thenReturn(Optional.of(user));

        UserDto result = userController.getById(id);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void testGetByIdNotFound() {
        long id = 1L;
        when(userService.getById(id)).thenReturn(Optional.empty());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            userController.getById(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testGetAll() {
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("User 1");

        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("User 2");

        List<UserDto> expectedUsers = Arrays.asList(user1, user2);

        when(userService.getAll()).thenReturn(expectedUsers);

        List<UserDto> result = userController.getAll();

        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
    }
}