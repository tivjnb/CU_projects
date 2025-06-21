package cu.projects.week8.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Timestamp orderDate;

    @NotBlank(message = "Order status is required")
    private String status;
}
