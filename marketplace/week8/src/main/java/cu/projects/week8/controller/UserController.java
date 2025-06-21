package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.UserDto;
import cu.projects.week8.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Users", description = "CRUD for users")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@Valid @RequestBody UserDto user) {
        return userService.add(user);
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable long id) {
        Optional<UserDto> user = userService.getById(id);
        if (user.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with this id not found"
            );
        }
        return user.get();
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable long id, @Valid @RequestBody UserDto user) {
        Optional<UserDto> existingUser = userService.getById(id);
        if (existingUser.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with this id not found"
            );
        }
        userService.update(id, user);
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        Optional<UserDto> existingUser = userService.getById(id);
        if (existingUser.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with this id not found"
            );
        }
        userService.delete(id);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}