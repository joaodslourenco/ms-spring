package com.e_commerce.products.controllers;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
import com.e_commerce.products.dtos.CategoryUpdateReqDto;
import com.e_commerce.products.models.CategoryModel;
import com.e_commerce.products.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/products/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryModel> save(@RequestBody @Valid CategoryCreateReqDto categoryCreateReqDto) {
        return new ResponseEntity<>(categoryService.save(categoryCreateReqDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryModel>> findAll() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<CategoryModel> update(@PathVariable UUID id, @RequestBody @Valid CategoryUpdateReqDto categoryUpdateReqDto) {
        return new ResponseEntity<>(categoryService.update(id, categoryUpdateReqDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
