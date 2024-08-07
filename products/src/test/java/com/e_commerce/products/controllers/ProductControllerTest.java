package com.e_commerce.products.controllers;

import com.e_commerce.products.dtos.ProductCreateReqDto;
import com.e_commerce.products.dtos.ProductUpdateReqDto;
import com.e_commerce.products.models.ProductModel;
import com.e_commerce.products.services.ProductService;
import com.e_commerce.products.util.ProductCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    @DisplayName("save returns product when successful")
    void save_ReturnsProduct_WhenSuccessful() {
        ProductCreateReqDto productCreateReqDto = ProductCreator.productCreateReqDto;
        ProductModel productModel = ProductCreator.validProduct;
        Mockito.when(productService.save(ArgumentMatchers.any(ProductCreateReqDto.class))).thenReturn(productModel);

        ResponseEntity<ProductModel> response = productController.save(productCreateReqDto);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(productModel, response.getBody());
    }

    @Test
    @DisplayName("findAllPaginated returns list of products when successful")
    void findAllPaginated_ReturnsListOfProducts_WhenSuccessful() {
        Page<ProductModel> productPage = ProductCreator.productModelPage;
        Mockito.when(productService.findAllPaginated(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).thenReturn(productPage);

        ResponseEntity<Page<ProductModel>> response = productController.findAllPaginated(0, 10, "");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(productPage, response.getBody());
    }

    @Test
    @DisplayName("findProductById returns product when successful")
    void findProductById_ReturnsProduct_WhenSuccessful() {
        ProductModel productModel = ProductCreator.validProduct;
        Mockito.when(productService.findById(any(UUID.class))).thenReturn(productModel);

        ResponseEntity<ProductModel> response = productController.findById(UUID.randomUUID().toString());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(productModel, response.getBody());
    }

    @Test
    @DisplayName("update modifies product when successful")
    void update_ModifiesProduct_WhenSuccessful() {
        ProductUpdateReqDto productUpdateReqDto = ProductCreator.productUpdateReqDto;
        ProductModel productModel = ProductCreator.validProduct;
        Mockito.when(productService.update(any(UUID.class), any(ProductUpdateReqDto.class))).thenReturn(productModel);

        ResponseEntity<ProductModel> response = productController.update(UUID.randomUUID().toString(), productUpdateReqDto);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(productModel, response.getBody());
    }

    @Test
    @DisplayName("delete removes product when successful")
    void delete_RemovesProduct_WhenSuccessful() {
        ResponseEntity<Void> response = productController.delete(UUID.randomUUID().toString());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}