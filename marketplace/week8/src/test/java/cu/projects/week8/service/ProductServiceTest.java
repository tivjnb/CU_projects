package cu.projects.week8.service;

import cu.projects.week8.model.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdd() {
        ProductDto product = new ProductDto();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("10.99"));
        product.setCategory("Test Category");

        productService.add(product);

        verify(jdbcTemplate, times(1)).update(
                anyString(),
                eq(product.getName()),
                eq(product.getDescription()),
                eq(product.getPrice()),
                eq(product.getCategory())
        );
    }

    @Test
    public void testGetById() {
        long id = 1L;
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(id);
        expectedProduct.setName("Test Product");
        expectedProduct.setDescription("Test Description");
        expectedProduct.setPrice(new BigDecimal("10.99"));
        expectedProduct.setCategory("Test Category");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(expectedProduct));

        List<ProductDto> result = productService.getById(id);

        assertEquals(1, result.size());
        assertEquals(expectedProduct.getId(), result.get(0).getId());
        assertEquals(expectedProduct.getName(), result.get(0).getName());
        assertEquals(expectedProduct.getDescription(), result.get(0).getDescription());
        assertEquals(expectedProduct.getPrice(), result.get(0).getPrice());
        assertEquals(expectedProduct.getCategory(), result.get(0).getCategory());

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    public void testUpdate() {
        long id = 1L;
        ProductDto product = new ProductDto();
        product.setName("Updated Product");
        product.setDescription("Updated Description");
        product.setPrice(new BigDecimal("20.99"));
        product.setCategory("Updated Category");

        productService.update(id, product);

        verify(jdbcTemplate, times(1)).update(
                anyString(),
                eq(product.getName()),
                eq(product.getDescription()),
                eq(product.getPrice()),
                eq(product.getCategory()),
                eq(id)
        );
    }

    @Test
    public void testDelete() {
        long id = 1L;
        productService.delete(id);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(id));
    }

    @Test
    public void testGetWithFilters() {
        String category = "test";
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");

        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setName("Test Product 1");
        product1.setPrice(new BigDecimal("15.99"));
        product1.setCategory("Test Category");

        ProductDto product2 = new ProductDto();
        product2.setId(2L);
        product2.setName("Test Product 2");
        product2.setPrice(new BigDecimal("25.99"));
        product2.setCategory("Test Category");

        List<ProductDto> expectedProducts = Arrays.asList(product1, product2);

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(expectedProducts);

        List<ProductDto> result = productService.getWithFilters(category, minPrice, maxPrice);

        assertEquals(2, result.size());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }
}