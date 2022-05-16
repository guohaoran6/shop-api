package io.recruitment.assessment.api.service.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.recruitment.assessment.api.dto.Product;
import io.recruitment.assessment.api.entity.ProductEntity;
import io.recruitment.assessment.api.repository.ProductRepository;
import io.recruitment.assessment.api.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
public class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;

    @SpyBean
    private ProductService productService;

    private ObjectMapper mapper = new ObjectMapper();

    protected ProductEntity productEntity;
    protected ProductEntity productEntity1;
    protected ProductEntity productEntity2;

    protected List<ProductEntity> productEntityList = new ArrayList<>();

    @BeforeEach
    public void init() throws Exception {
        productEntity = mapper.readValue("{\"productId\": 1, \"name\": \"entity 1\", \"desc\": \"desc 1\", \"imgUrl\": \"image 1\", \"price\": 10, \"stockNumber\": 10, \"tag\": \"tag 1\", \"deleteFlg\": 0, \"version\": 1}", ProductEntity.class);
        productEntity1 = mapper.readValue("{\"productId\": 2, \"name\": \"entity 2\", \"desc\": \"desc 2\", \"imgUrl\": \"image 2\", \"price\": 20, \"stockNumber\": 10, \"tag\": \"tag 2\", \"deleteFlg\": 0, \"version\": 2}", ProductEntity.class);
        productEntity2 = mapper.readValue("{\"productId\": 3, \"name\": \"entity 3\", \"desc\": \"desc 3\", \"imgUrl\": \"image 3\", \"price\": 30, \"stockNumber\": 10, \"tag\": \"tag 3\", \"deleteFlg\": 0, \"version\": 3}", ProductEntity.class);

        productEntityList.add(productEntity);
        productEntityList.add(productEntity1);
        productEntityList.add(productEntity2);
    }

    @Test
    void testSaveItemSuccess() {
        Mockito.when(productRepository.findById(anyInt())).thenReturn(productEntity);

        Product product = productService.getProductById(1);
        Assertions.assertEquals(1, product.getProductId());
    }

}

