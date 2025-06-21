package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.CreateOrderRequest;
import cu.projects.week8.model.OrderDto;
import cu.projects.week8.model.OrderItemDTO;
import cu.projects.week8.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Order", description = "CRUD api for orders")
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create order")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.getOrder(), request.getItems());
    }

    @Operation(summary = "Get order by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getById(@PathVariable long id) {
        Optional<OrderDto> order = orderService.getById(id);
        if (order.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order with this id not found"
            );
        }
        return order.get();
    }

    @Operation(summary = "Get all order items of order")
    @GetMapping("/{id}/items")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemDTO> getOrderItems(@PathVariable long id) {
        Optional<OrderDto> order = orderService.getById(id);
        if (order.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order with this id not found"
            );
        }
        return orderService.getOrderItems(id);
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(@PathVariable long id, @RequestBody String status) {
        Optional<OrderDto> order = orderService.getById(id);
        if (order.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order with this id not found"
            );
        }
        orderService.updateOrderStatus(id, status);
    }

    @Operation(summary = "Delete order")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable long id) {
        Optional<OrderDto> order = orderService.getById(id);
        if (order.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order with this id not found"
            );
        }
        orderService.deleteOrder(id);
    }

    @Operation(summary = "Get orders by user id")
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrdersByUserId(@PathVariable long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @Operation(summary = "Get all orders")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Get all orders with status")
    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status);
    }
}