package com.e_commerce.products.controllers;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
import com.e_commerce.products.dtos.CategoryUpdateReqDto;
import com.e_commerce.products.models.CategoryModel;
import com.e_commerce.products.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/products/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Endpoints for category management")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Creates a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Category already exists")
    })
    public ResponseEntity<CategoryModel> save(@RequestBody @Valid CategoryCreateReqDto categoryCreateReqDto) {
        return new ResponseEntity<>(categoryService.save(categoryCreateReqDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all categories")
    @ApiResponse(responseCode = "200", description = "List of categories")
    public ResponseEntity<List<CategoryModel>> findAll() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "400", description = "Category not found")
    })
    public ResponseEntity<CategoryModel> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    @Operation(summary = "Update category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Category not found")
    })
    public ResponseEntity<CategoryModel> update(@PathVariable UUID id, @RequestBody @Valid CategoryUpdateReqDto categoryUpdateReqDto) {
        return new ResponseEntity<>(categoryService.update(id, categoryUpdateReqDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Category not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
