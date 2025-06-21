package cu.projects.week8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(System.getenv("JDBC_URL") != null
                ? System.getenv("JDBC_URL")
                : "jdbc:postgresql://localhost:5432/mydatabase");
        dataSource.setUsername(System.getenv("DB_USERNAME") != null
                ? System.getenv("DB_USERNAME")
                : "myuser");
        dataSource.setPassword(System.getenv("DB_PASSWORD") != null
                ? System.getenv("DB_PASSWORD")
                : "mypassword");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                   id SERIAL PRIMARY KEY,
                   name TEXT NOT NULL,
                   email TEXT NOT NULL,
                   address TEXT,
                   phone TEXT
               );
                """;
        jdbcTemplate.execute(createUsersTable);

        String createProductTable = """
                CREATE TABLE IF NOT EXISTS products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    price DECIMAL(10, 2) NOT NULL,
                    category VARCHAR(255)
                );
                """;
        jdbcTemplate.execute(createProductTable);

        String createOrderTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    id SERIAL PRIMARY KEY,
                    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                    order_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(50) NOT NULL
                );
                """;
        jdbcTemplate.execute(createOrderTable);

        String createOrderItemTable = """
                CREATE TABLE IF NOT EXISTS order_items (
                    id SERIAL PRIMARY KEY,
                    order_id INTEGER REFERENCES orders(id) ON DELETE CASCADE,
                    product_id INTEGER REFERENCES products(id) ON DELETE CASCADE,
                    quantity INTEGER NOT NULL CHECK (quantity > 0),
                    price DECIMAL(10, 2) NOT NULL
                );
                """;
        jdbcTemplate.execute(createOrderItemTable);

        return jdbcTemplate;
    }
}