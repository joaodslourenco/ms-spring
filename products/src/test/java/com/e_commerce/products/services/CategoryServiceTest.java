package com.e_commerce.products.services;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
import com.e_commerce.products.dtos.CategoryUpdateReqDto;
import com.e_commerce.products.mappers.CategoryMapper;
import com.e_commerce.products.models.CategoryModel;
import com.e_commerce.products.repositories.CategoryRepository;
import com.e_commerce.products.util.CategoryCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;


    @Test
    @DisplayName("Save creates category when successful")
    void save_CreatesCategory_WhenSuccessful() {
        CategoryCreateReqDto categoryCreateReqDto = CategoryCreator.categoryCreateReqDto;
        CategoryModel categoryModel = CategoryCreator.validCategory;
        Mockito.when(categoryRepository.findByNameCaseInsensitive(anyString())).thenReturn(null);
        Mockito.when(categoryMapper.toCategoryModel(any(CategoryCreateReqDto.class))).thenReturn(categoryModel);
        Mockito.when(categoryRepository.save(any(CategoryModel.class))).thenReturn(categoryModel);

        CategoryModel result = categoryService.save(categoryCreateReqDto);

        Assertions.assertEquals(categoryModel, result);
    }

    @Test
    @DisplayName("Save throws RuntimeException when category already exists")
    void save_ThrowsRuntimeException_WhenCategoryAlreadyExists() {
        CategoryCreateReqDto categoryCreateReqDto = CategoryCreator.categoryCreateReqDto;
        CategoryModel existingCategory = new CategoryModel();
        Mockito.when(categoryRepository.findByNameCaseInsensitive(anyString())).thenReturn(existingCategory);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.save(categoryCreateReqDto);
        });

        Assertions.assertEquals("Category already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Find category by id returns category when successful")
    void findById_ReturnsCategory_WhenSuccessful() {
        UUID id = UUID.randomUUID();
        CategoryModel categoryModel = new CategoryModel();
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryModel));

        CategoryModel result = categoryService.findById(id);

        Assertions.assertEquals(categoryModel, result);
    }

    @Test
    @DisplayName("Find category by id throws RuntimeException when category is not found")
    void findById_ThrowsRuntimeException_WhenCategoryIsNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.findById(id);
        });

        Assertions.assertEquals("Category not found", exception.getMessage());
    }

    @Test
    @DisplayName("Find all returns list of categories when successful")
    void findAll_ReturnsListOfCategories_WhenSuccessful() {
        CategoryModel categoryModel = CategoryCreator.validCategory;
        Mockito.when(categoryRepository.findAll()).thenReturn(CategoryCreator.listOfCategories);

        var result = categoryService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(categoryModel, result.getFirst());
    }


    @Test
    @DisplayName("Update category modifies category when successful")
    void update_ModifiesCategory_WhenSuccessful() {
        UUID id = UUID.randomUUID();
        CategoryUpdateReqDto categoryUpdateReqDto = CategoryUpdateReqDto.builder().name("new name").build();
        CategoryModel existingCategory = new CategoryModel();
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepository.save(any(CategoryModel.class))).thenReturn(existingCategory);

        CategoryModel result = categoryService.update(id, categoryUpdateReqDto);

        Assertions.assertEquals(existingCategory, result);
    }

    @Test
    @DisplayName("Delete removes category when successful")
    void delete_RemovesCategory_WhenSuccessful() {
        UUID id = UUID.randomUUID();
        CategoryModel categoryModel = new CategoryModel();
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryModel));
        Mockito.doNothing().when(categoryRepository).delete(categoryModel);

        categoryService.delete(id);

        Mockito.verify(categoryRepository, Mockito.times(1)).delete(categoryModel);
    }

}