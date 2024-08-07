package com.e_commerce.products.services;

import com.e_commerce.products.dtos.ProductCreateReqDto;
import com.e_commerce.products.dtos.ProductUpdateReqDto;
import com.e_commerce.products.mappers.ProductMapper;
import com.e_commerce.products.models.ProductModel;
import com.e_commerce.products.repositories.ProductRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;


    @Test
    @DisplayName("Save returns product when successful")
    void save_ReturnsProduct_WhenSuccessful() {
        ProductCreateReqDto productCreateReqDto = ProductCreator.productCreateReqDto;
        ProductModel productModel = ProductCreator.validProduct;
        Mockito.when(productMapper.toProductModel(ArgumentMatchers.any(ProductCreateReqDto.class))).thenReturn(productModel);
        Mockito.when(categoryService.findById(ArgumentMatchers.any(UUID.class))).thenReturn(productModel.getCategory());
        Mockito.when(productRepository.save(ArgumentMatchers.any(ProductModel.class))).thenReturn(productModel);

        ProductModel result = productService.save(productCreateReqDto);

        Assertions.assertEquals(productModel, result);
    }

    @Test
    @DisplayName("Save throws RuntimeException when category is not found")
    void save_ThrowsRuntimeException_WhenCategoryIsNotFound() {
        ProductCreateReqDto productCreateReqDto = ProductCreator.productCreateReqDto;
        Mockito.when(categoryService.findById(ArgumentMatchers.any(UUID.class))).thenThrow(new RuntimeException("Category not found"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> productService.save(productCreateReqDto));

        Assertions.assertEquals("Category not found", exception.getMessage());
    }

    @Test
    @DisplayName("Find by id returns product when successful")
    void findById_ReturnsProduct_WhenSuccessful() {
        ProductModel productModel = ProductCreator.validProduct;
        Mockito.when(productRepository.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(productModel));

        ProductModel result = productService.findById(UUID.randomUUID());

        Assertions.assertEquals(productModel, result);
    }

    @Test
    @DisplayName("Find by id throws RuntimeException when product is not found")
    void findById_ThrowsRuntimeException_WhenProductIsNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> productService.findById(id));

        Assertions.assertEquals("Product not found", exception.getMessage());
    }

    @Test
    @DisplayName("Find all paginated returns list of products when successful")
    void findAllPaginated_ReturnsListOfProducts_WhenSuccessful() {
        Page<ProductModel> productPage = ProductCreator.productModelPage;
        Mockito.when(productRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(productPage);

        Page<ProductModel> result = productService.findAllPaginated(0, 10, null);

        Assertions.assertEquals(productPage, result);
    }

    @Test
    @DisplayName("Find all paginated returns empty page when no products are found")
    void findAllPaginated_ReturnsEmptyPage_WhenNoProductsAreFound() {
        Page<ProductModel> emptyPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(productRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(emptyPage);

        Page<ProductModel> result = productService.findAllPaginated(0, 10, null);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Update modifies product when successful")
    void update_ModifiesProduct_WhenSuccessful() {
        ProductUpdateReqDto productUpdateReqDto = ProductCreator.productUpdateReqDto;
        ProductModel productModel = ProductCreator.validProduct;
        Mockito.when(productRepository.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(productModel));
        Mockito.when(productRepository.save(ArgumentMatchers.any(ProductModel.class))).thenReturn(productModel);

        ProductModel result = productService.update(UUID.randomUUID(), productUpdateReqDto);

        Assertions.assertEquals(productModel, result);
    }

    @Test
    @DisplayName("Update throws RuntimeException when product is not found")
    void update_ThrowsRuntimeException_WhenProductIsNotFound() {
        UUID id = UUID.randomUUID();
        ProductUpdateReqDto productUpdateReqDto = ProductCreator.productUpdateReqDto;
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> productService.update(id, productUpdateReqDto));


        Assertions.assertEquals("Product not found", exception.getMessage());
    }

    @Test
    @DisplayName("Delete removes product when successful")
    void delete_RemovesProduct_WhenSuccessful() {
        ProductModel productModel = ProductCreator.validProduct;
        Mockito.when(productRepository.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(productModel));

        productService.delete(UUID.randomUUID());

        Mockito.verify(productRepository, Mockito.times(1)).delete(productModel);
    }

    @Test
    @DisplayName("Delete throws RuntimeException when product is not found")
    void delete_ThrowsRuntimeException_WhenProductIsNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> productService.delete(id));

        Assertions.assertEquals("Product not found", exception.getMessage());
    }
}