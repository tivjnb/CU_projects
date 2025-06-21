package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.OrderItemDTO;
import cu.projects.week8.service.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderItemControllerTest {

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrderItem() {
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setOrderId(1L);
        orderItem.setProductId(1L);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("10.99"));

        when(orderItemService.addOrderItem(orderItem)).thenReturn(1L);

        Long result = orderItemController.addOrderItem(orderItem);

        assertEquals(1L, result);
        verify(orderItemService, times(1)).addOrderItem(orderItem);
    }

    @Test
    public void testGetById() {
        long id = 1L;
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setId(id);
        orderItem.setOrderId(1L);
        orderItem.setProductId(1L);
        orderItem.setQuantity(2);
        orderItem.setPrice(new BigDecimal("10.99"));

        when(orderItemService.getById(id)).thenReturn(Optional.of(orderItem));

        OrderItemDTO result = orderItemController.getById(id);

        assertEquals(orderItem.getId(), result.getId());
        assertEquals(orderItem.getOrderId(), result.getOrderId());
        assertEquals(orderItem.getProductId(), result.getProductId());
        assertEquals(orderItem.getQuantity(), result.getQuantity());
        assertEquals(orderItem.getPrice(), result.getPrice());
    }

    @Test
    public void testGetByIdNotFound() {
        long id = 1L;
        when(orderItemService.getById(id)).thenReturn(Optional.empty());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            orderItemController.getById(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testUpdateOrderItem() {
        long id = 1L;
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setQuantity(3);
        orderItem.setPrice(new BigDecimal("12.99"));

        when(orderItemService.getById(id)).thenReturn(Optional.of(new OrderItemDTO()));

        orderItemController.updateOrderItem(id, orderItem);

        verify(orderItemService, times(1)).updateOrderItem(id, orderItem);
    }

    @Test
    public void testDeleteOrderItem() {
        long id = 1L;

        when(orderItemService.getById(id)).thenReturn(Optional.of(new OrderItemDTO()));

        orderItemController.deleteOrderItem(id);

        verify(orderItemService, times(1)).deleteOrderItem(id);
    }
}