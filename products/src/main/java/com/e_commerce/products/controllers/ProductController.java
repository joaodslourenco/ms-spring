package com.e_commerce.products.controllers;

import com.e_commerce.products.dtos.ProductCreateReqDto;
import com.e_commerce.products.dtos.ProductUpdateReqDto;
import com.e_commerce.products.exceptions.details.BadRequestExceptionDetails;
import com.e_commerce.products.models.ProductModel;
import com.e_commerce.products.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Endpoints for product management")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Creates a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Category not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))
            )
    })
    public ResponseEntity<ProductModel> save(@RequestBody @Valid ProductCreateReqDto productCreateReqDto) {
        return new ResponseEntity<>(productService.save(productCreateReqDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all products paginated")
    @ApiResponse(responseCode = "200", description = "List of products paginated")
    public ResponseEntity<Page<ProductModel>> findAllPaginated(
            @Parameter(description = "Page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantity of products to be returned") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Find Products By Category") @RequestParam(required = false) String categoryId) {
        return new ResponseEntity<>(productService.findAllPaginated(page, size, categoryId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "400", description = "Product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))
            )
    })
    public ResponseEntity<ProductModel> findById(@PathVariable String id) {
        return new ResponseEntity<>(productService.findById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))
            )
    })
    public ResponseEntity<ProductModel> update(@PathVariable String id, @RequestBody @Valid ProductUpdateReqDto productUpdateReqDto) {
        return new ResponseEntity<>(productService.update(UUID.fromString(id), productUpdateReqDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))
            )
    })
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
