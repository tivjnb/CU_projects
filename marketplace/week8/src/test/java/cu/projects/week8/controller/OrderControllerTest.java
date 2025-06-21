package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.CreateOrderRequest;
import cu.projects.week8.model.OrderDto;
import cu.projects.week8.model.OrderItemDTO;
import cu.projects.week8.service.OrderItemService;
import cu.projects.week8.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        OrderDto order = new OrderDto();
        order.setUserId(1L);
        order.setStatus("NEW");

        OrderItemDTO item = new OrderItemDTO();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("10.99"));

        List<OrderItemDTO> items = Arrays.asList(item);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setOrder(order);
        request.setItems(items);

        when(orderService.createOrder(order, items)).thenReturn(1L);

        Long result = orderController.createOrder(request);

        assertEquals(1L, result);
        verify(orderService, times(1)).createOrder(order, items);
    }

    @Test
    public void testGetById() {
        long id = 1L;
        OrderDto order = new OrderDto();
        order.setId(id);
        order.setUserId(1L);
        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        order.setStatus("NEW");

        when(orderService.getById(id)).thenReturn(Optional.of(order));

        OrderDto result = orderController.getById(id);

        assertEquals(order.getId(), result.getId());
        assertEquals(order.getUserId(), result.getUserId());
        assertEquals(order.getStatus(), result.getStatus());
    }

    @Test
    public void testGetByIdNotFound() {
        long id = 1L;
        when(orderService.getById(id)).thenReturn(Optional.empty());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            orderController.getById(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testGetOrderItems() {
        long id = 1L;
        OrderDto order = new OrderDto();
        order.setId(id);

        OrderItemDTO item1 = new OrderItemDTO();
        item1.setId(1L);
        item1.setOrderId(id);
        item1.setProductId(1L);

        OrderItemDTO item2 = new OrderItemDTO();
        item2.setId(2L);
        item2.setOrderId(id);
        item2.setProductId(2L);

        List<OrderItemDTO> expectedItems = Arrays.asList(item1, item2);

        when(orderService.getById(id)).thenReturn(Optional.of(order));
        when(orderService.getOrderItems(id)).thenReturn(expectedItems);

        List<OrderItemDTO> result = orderController.getOrderItems(id);

        assertEquals(2, result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item2.getId(), result.get(1).getId());
    }

    @Test
    public void testUpdateOrderStatus() {
        long id = 1L;
        String status = "COMPLETED";

        OrderDto order = new OrderDto();
        order.setId(id);
        order.setStatus("NEW");

        when(orderService.getById(id)).thenReturn(Optional.of(order));

        orderController.updateOrderStatus(id, status);

        verify(orderService, times(1)).updateOrderStatus(id, status);
    }

    @Test
    public void testDeleteOrder() {
        long id = 1L;

        OrderDto order = new OrderDto();
        order.setId(id);

        when(orderService.getById(id)).thenReturn(Optional.of(order));

        orderController.deleteOrder(id);

        verify(orderService, times(1)).deleteOrder(id);
    }

    @Test
    public void testGetOrdersByUserId() {
        long userId = 1L;

        OrderDto order1 = new OrderDto();
        order1.setId(1L);
        order1.setUserId(userId);

        OrderDto order2 = new OrderDto();
        order2.setId(2L);
        order2.setUserId(userId);

        List<OrderDto> expectedOrders = Arrays.asList(order1, order2);

        when(orderService.getOrdersByUserId(userId)).thenReturn(expectedOrders);

        List<OrderDto> result = orderController.getOrdersByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(order1.getId(), result.get(0).getId());
        assertEquals(order2.getId(), result.get(1).getId());
    }
}