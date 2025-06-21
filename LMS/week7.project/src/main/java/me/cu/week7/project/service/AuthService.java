package me.cu.week7.project.service;

import me.cu.week7.project.exceptions.HttpStatusException;
import me.cu.week7.project.enums.Role;
import me.cu.week7.project.model.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AuthService {
    private final HashMap<Long, UserDto> users = new HashMap<>();
    private final AtomicLong nextId = new AtomicLong(0);

    public static final List<Role> adminOnly= List.of(Role.ADMIN);
    public static final List<Role> adminAndMentor= List.of(Role.ADMIN, Role.MENTOR);
    public static final List<Role> allUsers= List.of(Role.ADMIN, Role.MENTOR, Role.STUDENT);

    public void signIn(UserDto newUser) {
        long id = nextId.getAndIncrement();
        newUser.setId(id);
        users.put(id, newUser);
    }

    public UserDto auth (String basic) {
        String decoded = new String(Base64.getDecoder().decode(basic));
        int splitIndex = decoded.indexOf(':');
        if (splitIndex == -1) {
            throw new HttpStatusException(
                    HttpStatus.FORBIDDEN,
                    "Invalid auth format"
            );
        }
        String login = decoded.substring(0, splitIndex);
        String password = decoded.substring(splitIndex + 1);

        for (UserDto user : users.values()) {
            if (user.getLogin().equals(login)) {
                if (user.getPassword().equals(password)) {
                    return user;
                } else {
                    throw new HttpStatusException(
                            HttpStatus.FORBIDDEN,
                            "Invalid password"
                    );
                }

            }
        }
        throw new HttpStatusException(
                HttpStatus.FORBIDDEN,
                "User not found"
        );
    }

    public void basicAuth(String basic, List<Role> rolesWithAccess) {
        if (basic.isBlank()) {
            throw new HttpStatusException(
                    HttpStatus.FORBIDDEN,
                    "Empty basic header"
            );
        }
        UserDto user = auth(basic);
        if (!rolesWithAccess.contains(user.getRole())) {
            throw new HttpStatusException(
                    HttpStatus.FORBIDDEN,
                    "Unresolved operation"
            );
        }
    }
}
