package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.OrderItemDTO;
import cu.projects.week8.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order-item")
@Tag(name = "Order item", description = "CRUD for order items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Operation(summary = "Create new order item")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addOrderItem(@Valid @RequestBody OrderItemDTO orderItem) {
        return orderItemService.addOrderItem(orderItem);
    }

    @Operation(summary = "Get order item by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderItemDTO getById(@PathVariable long id) {
        Optional<OrderItemDTO> orderItem = orderItemService.getById(id);
        if (orderItem.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item with this id not found"
            );
        }
        return orderItem.get();
    }

    @Operation(summary = "Update order item")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderItem(@PathVariable long id, @Valid @RequestBody OrderItemDTO orderItem) {
        Optional<OrderItemDTO> existingItem = orderItemService.getById(id);
        if (existingItem.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item with this id not found"
            );
        }
        orderItemService.updateOrderItem(id, orderItem);
    }

    @Operation(summary = "Delete order item")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderItem(@PathVariable long id) {
        Optional<OrderItemDTO> existingItem = orderItemService.getById(id);
        if (existingItem.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Order item with this id not found"
            );
        }
        orderItemService.deleteOrderItem(id);
    }
}