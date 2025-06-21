package cu.projects.week8.service;

import cu.projects.week8.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdd() {
        UserDto user = new UserDto();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAddress("Test Address");
        user.setPhone("+1234567890");

        // Настройка мока для jdbcTemplate.update с PreparedStatementCreator
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            java.util.Map<String, Object> keys = new java.util.HashMap<>();
            keys.put("id", 1L);
            ((GeneratedKeyHolder)keyHolder).getKeyList().add(keys);
            return 1;
        });

        Long result = userService.add(user);

        assertEquals(1L, result);
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
    }

    @Test
    public void testGetById() {
        long id = 1L;
        UserDto expectedUser = new UserDto();
        expectedUser.setId(id);
        expectedUser.setName("Test User");
        expectedUser.setEmail("test@example.com");
        expectedUser.setAddress("Test Address");
        expectedUser.setPhone("+1234567890");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(expectedUser));

        Optional<UserDto> result = userService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(expectedUser.getId(), result.get().getId());
        assertEquals(expectedUser.getName(), result.get().getName());
        assertEquals(expectedUser.getEmail(), result.get().getEmail());

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }
}