package cu.projects.week8.service;

import cu.projects.week8.model.OrderDto;
import cu.projects.week8.model.OrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional // Чтобы не оставалось неполных данных из-за ошибок
    public Long createOrder(OrderDto order, List<OrderItemDTO> items) {
        String orderSql = "INSERT INTO orders (user_id, order_date, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getUserId());
            ps.setTimestamp(2, order.getOrderDate() != null ?
                    order.getOrderDate() : Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, order.getStatus());
            return ps;
        }, keyHolder);

        Long orderId = keyHolder.getKey().longValue();

        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        for (OrderItemDTO item : items) {
            jdbcTemplate.update(
                    itemSql,
                    orderId,
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice()
            );
        }

        return orderId;
    }

    public Optional<OrderDto> getById(long id) {
        String sql = "SELECT id, user_id, order_date, status FROM orders WHERE id = ?";
        List<OrderDto> orders = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            OrderDto order = new OrderDto();
            order.setId(rs.getLong("id"));
            order.setUserId(rs.getLong("user_id"));
            order.setOrderDate(rs.getTimestamp("order_date"));
            order.setStatus(rs.getString("status"));
            return order;
        });

        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }

    public List<OrderItemDTO> getOrderItems(long orderId) {
        String sql = "SELECT id, order_id, product_id, quantity, price FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) -> {
            OrderItemDTO item = new OrderItemDTO();
            item.setId(rs.getLong("id"));
            item.setOrderId(rs.getLong("order_id"));
            item.setProductId(rs.getLong("product_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getBigDecimal("price"));
            return item;
        });
    }

    public void updateOrderStatus(long id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }

    public List<OrderDto> getOrdersByUserId(long userId) {
        String sql = "SELECT id, user_id, order_date, status FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            OrderDto order = new OrderDto();
            order.setId(rs.getLong("id"));
            order.setUserId(rs.getLong("user_id"));
            order.setOrderDate(rs.getTimestamp("order_date"));
            order.setStatus(rs.getString("status"));
            return order;
        });
    }

    @Transactional
    public void deleteOrder(long id) {
        String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";
        jdbcTemplate.update(deleteItemsSql, id);

        String deleteOrderSql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(deleteOrderSql, id);
    }

    public List<OrderDto> getAllOrders() {
        String sql = "SELECT id, user_id, order_date, status FROM orders ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            OrderDto order = new OrderDto();
            order.setId(rs.getLong("id"));
            order.setUserId(rs.getLong("user_id"));
            order.setOrderDate(rs.getTimestamp("order_date"));
            order.setStatus(rs.getString("status"));
            return order;
        });
    }

    public List<OrderDto> getOrdersByStatus(String status) {
        String sql = "SELECT id, user_id, order_date, status FROM orders WHERE status = ? ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, new Object[]{status}, (rs, rowNum) -> {
            OrderDto order = new OrderDto();
            order.setId(rs.getLong("id"));
            order.setUserId(rs.getLong("user_id"));
            order.setOrderDate(rs.getTimestamp("order_date"));
            order.setStatus(rs.getString("status"));
            return order;
        });
    }
}