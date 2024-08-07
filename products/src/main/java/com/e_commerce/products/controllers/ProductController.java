package com.e_commerce.products.controllers;

import com.e_commerce.products.dtos.ProductCreateReqDto;
import com.e_commerce.products.dtos.ProductUpdateReqDto;
import com.e_commerce.products.models.ProductModel;
import com.e_commerce.products.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductModel> save(@RequestBody @Valid ProductCreateReqDto productCreateReqDto) {
        return new ResponseEntity<>(productService.save(productCreateReqDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ProductModel>> findAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String categoryId) {
        return new ResponseEntity<>(productService.findAllPaginated(page, size, categoryId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> findById(@PathVariable String id) {
        return new ResponseEntity<>(productService.findById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductModel> update(@PathVariable String id, @RequestBody @Valid ProductUpdateReqDto productUpdateReqDto) {
        return new ResponseEntity<>(productService.update(UUID.fromString(id), productUpdateReqDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
