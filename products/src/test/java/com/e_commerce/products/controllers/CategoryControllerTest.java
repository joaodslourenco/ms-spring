package com.e_commerce.products.controllers;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
import com.e_commerce.products.dtos.CategoryUpdateReqDto;
import com.e_commerce.products.models.CategoryModel;
import com.e_commerce.products.services.CategoryService;
import com.e_commerce.products.util.CategoryCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;


    @Test
    @DisplayName("save creates category when successful")
    void save_CreatesCategory_WhenSuccessful() {
        CategoryCreateReqDto categoryCreateReqDto = CategoryCreator.categoryCreateReqDto;
        CategoryModel categoryModel = CategoryCreator.validCategory;
        Mockito.when(categoryService.save(any(CategoryCreateReqDto.class))).thenReturn(categoryModel);

        ResponseEntity<CategoryModel> response = categoryController.save(categoryCreateReqDto);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(categoryModel, response.getBody());
    }

    @Test
    @DisplayName("findById returns category when successful")
    void findById_ReturnsCategory_WhenSuccessful() {
        UUID id = UUID.randomUUID();
        CategoryModel categoryModel = CategoryCreator.validCategory;
        Mockito.when(categoryService.findById(id)).thenReturn(categoryModel);

        ResponseEntity<CategoryModel> response = categoryController.findById(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(categoryModel, response.getBody());
    }

    @Test
    @DisplayName("findAll returns list of categories when successful")
    void findAll_ReturnsListOfCategories_WhenSuccessful() {
        CategoryModel categoryModel = CategoryCreator.validCategory;
        Mockito.when(categoryService.findAll()).thenReturn(CategoryCreator.listOfCategories);

        ResponseEntity<List<CategoryModel>> response = categoryController.findAll();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(categoryModel, response.getBody().getFirst());
    }

    @Test
    @DisplayName("update modifies category when successful")
    void update_ModifiesCategory_WhenSuccessful() {
        UUID id = UUID.randomUUID();
        CategoryUpdateReqDto categoryUpdateReqDto = CategoryCreator.categoryUpdateReqDto;
        CategoryModel categoryModel = CategoryCreator.validCategory;
        Mockito.when(categoryService.update(any(UUID.class), any(CategoryUpdateReqDto.class))).thenReturn(categoryModel);

        ResponseEntity<CategoryModel> response = categoryController.update(id, categoryUpdateReqDto);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(categoryModel, response.getBody());
    }

    @Test
    @DisplayName("delete removes category when successful")
    void deleteCategorySuccessfully() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(categoryService).delete(id);

        ResponseEntity<Void> response = categoryController.delete(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(categoryService, Mockito.times(1)).delete(id);
    }
}