package cu.projects.week8.service;

import cu.projects.week8.model.OrderDto;
import cu.projects.week8.model.OrderItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        OrderDto order = new OrderDto();
        order.setUserId(1L);
        order.setStatus("NEW");

        OrderItemDTO item1 = new OrderItemDTO();
        item1.setProductId(1L);
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("10.99"));

        OrderItemDTO item2 = new OrderItemDTO();
        item2.setProductId(2L);
        item2.setQuantity(1);
        item2.setPrice(new BigDecimal("15.99"));

        List<OrderItemDTO> items = Arrays.asList(item1, item2);

        // Настройка мока для jdbcTemplate.update с PreparedStatementCreator
        when(jdbcTemplate.update(any(), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            java.util.Map<String, Object> keys = new java.util.HashMap<>();
            keys.put("id", 1L);
            ((GeneratedKeyHolder)keyHolder).getKeyList().add(keys);
            return 1;
        });

        Long result = orderService.createOrder(order, items);

        assertEquals(1L, result);
        verify(jdbcTemplate, times(1)).update(any(), any(KeyHolder.class));
        verify(jdbcTemplate, times(2)).update(anyString(), any(), any(), any(), any());
    }

    @Test
    public void testGetById() {
        long id = 1L;
        OrderDto expectedOrder = new OrderDto();
        expectedOrder.setId(id);
        expectedOrder.setUserId(1L);
        expectedOrder.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        expectedOrder.setStatus("NEW");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(expectedOrder));

        Optional<OrderDto> result = orderService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(expectedOrder.getId(), result.get().getId());
        assertEquals(expectedOrder.getUserId(), result.get().getUserId());
        assertEquals(expectedOrder.getStatus(), result.get().getStatus());

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }
}