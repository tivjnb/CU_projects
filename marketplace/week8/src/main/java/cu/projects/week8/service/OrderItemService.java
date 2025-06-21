package cu.projects.week8.service;

import cu.projects.week8.model.OrderItemDTO;
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
public class OrderItemService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderItemService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addOrderItem(OrderItemDTO orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, orderItem.getOrderId());
            ps.setLong(2, orderItem.getProductId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setBigDecimal(4, orderItem.getPrice());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<OrderItemDTO> getById(long id) {
        String sql = "SELECT id, order_id, product_id, quantity, price FROM order_items WHERE id = ?";
        List<OrderItemDTO> items = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            OrderItemDTO item = new OrderItemDTO();
            item.setId(rs.getLong("id"));
            item.setOrderId(rs.getLong("order_id"));
            item.setProductId(rs.getLong("product_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getBigDecimal("price"));
            return item;
        });

        return items.isEmpty() ? Optional.empty() : Optional.of(items.get(0));
    }

    public void updateOrderItem(long id, OrderItemDTO orderItem) {
        String sql = "UPDATE order_items SET quantity = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                orderItem.getQuantity(),
                orderItem.getPrice(),
                id
        );
    }

    public void deleteOrderItem(long id) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}