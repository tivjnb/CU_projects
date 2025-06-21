package cu.projects.week8.service;

import cu.projects.week8.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long add(UserDto user) {
        String sql = "INSERT INTO users (name, email, address, phone) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getPhone());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<UserDto> getById(long id) {
        String sql = "SELECT id, name, email, address, phone FROM users WHERE id = ?";
        List<UserDto> users = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            UserDto user = new UserDto();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setAddress(rs.getString("address"));
            user.setPhone(rs.getString("phone"));
            return user;
        });

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public void update(long id, UserDto user) {
        String sql = "UPDATE users SET name = ?, email = ?, address = ?, phone = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                user.getName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                id
        );
    }

    public void delete(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<UserDto> getAll() {
        String sql = "SELECT id, name, email, address, phone FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserDto user = new UserDto();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setAddress(rs.getString("address"));
            user.setPhone(rs.getString("phone"));
            return user;
        });
    }
}