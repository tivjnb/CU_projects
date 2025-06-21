package cu.projects.week8.controller;

import cu.projects.week8.exceptions.HttpStatusException;
import cu.projects.week8.model.ProductDto;
import cu.projects.week8.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        ProductDto product = new ProductDto();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.99"));

        productController.create(product);

        verify(productService, times(1)).add(product);
    }

    @Test
    public void testGetById() {
        long id = 1L;
        ProductDto product = new ProductDto();
        product.setId(id);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10.99"));

        when(productService.getById(id)).thenReturn(Arrays.asList(product));

        ProductDto result = productController.getById(id);

        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
    }

    @Test
    public void testGetByIdNotFound() {
        long id = 1L;
        when(productService.getById(id)).thenReturn(Collections.emptyList());

        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            productController.getById(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void testGetProducts() {
        String category = "test";
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");

        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setName("Test Product 1");
        product1.setPrice(new BigDecimal("15.99"));

        ProductDto product2 = new ProductDto();
        product2.setId(2L);
        product2.setName("Test Product 2");
        product2.setPrice(new BigDecimal("25.99"));

        List<ProductDto> expectedProducts = Arrays.asList(product1, product2);

        when(productService.getWithFilters(category, minPrice, maxPrice))
                .thenReturn(expectedProducts);

        List<ProductDto> result = productController.getProducts(category, minPrice, maxPrice);

        assertEquals(2, result.size());
        assertEquals(product1.getId(), result.get(0).getId());
        assertEquals(product2.getId(), result.get(1).getId());
    }
}