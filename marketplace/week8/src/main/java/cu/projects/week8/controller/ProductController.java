package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.ProductDto;
import cu.projects.week8.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products", description = "CRUD for products")
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController (ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create new product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody ProductDto product) {
        productService.add(product);
    }

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void put(
            @PathVariable long id,
            @Valid @RequestBody ProductDto product
    ) {
        productService.update(id, product);
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable long id
    ) {
        productService.delete(id);
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getById(
            @PathVariable long id
    ) {
        List<ProductDto> products  = productService.getById(id);
        if (products.isEmpty()) {
            throw new HttpStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product with this id not found"
            );
        }
        return products.get(0);
    }

    @Operation(summary = "Get product with minPrice maxPrice and category")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProducts(
            @RequestParam(value = "category", required = false, defaultValue = "") String category,
            @RequestParam(value = "minPrice", required = false, defaultValue = "0") BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "-1") BigDecimal maxPrice
            ) {
        return productService.getWithFilters(category, minPrice, maxPrice);
    }

}
