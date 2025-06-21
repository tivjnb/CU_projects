package cu.projects.week8.service;


import cu.projects.week8.model.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private static final BigDecimal INFINITY  = BigDecimal.valueOf(-1);  // Для отключения верхней границы в фильтрах
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductService (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(ProductDto product) {
        String sql = "INSERT INTO products " +
                "(name, description, price, category) " +
                "VALUES (?, ?, ?, ?) ";
        jdbcTemplate.update(
                sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory()
        );
    }

    public List<ProductDto> getById(long id) {
        String sql = "SELECT id, name, description, price, category  FROM products WHERE id =  ?";
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            ProductDto product = new ProductDto();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setCategory(rs.getString("category"));
            return product;
        });
    }

    public void update(long id, ProductDto product) {
        String sql = "UPDATE products SET " +
                "name = ?, description = ?, price = ? , category = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
                sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                id
        );
    }

    public void delete(long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<ProductDto> getWithFilters(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT id, name, description, price, category  " +
                "FROM products " +
                "WHERE LOWER(category) LIKE ? " +
                "AND price BETWEEN ? AND ? ";
        return jdbcTemplate.query(
                sql,
                new Object[]{
                        category,
                        minPrice,
                        (maxPrice.equals(INFINITY)) ? "''Infinity''" : maxPrice // PostgreSQL поддерживает Infinity
                },
                (rs, rowNum) -> {
                    ProductDto product = new ProductDto();
                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setCategory(rs.getString("category"));
                    return product;
                });
    }
}
