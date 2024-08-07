package com.e_commerce.products.services;

import com.e_commerce.products.dtos.ProductCreateReqDto;
import com.e_commerce.products.dtos.ProductUpdateReqDto;
import com.e_commerce.products.exceptions.BadRequestException;
import com.e_commerce.products.mappers.ProductMapper;
import com.e_commerce.products.models.ProductModel;
import com.e_commerce.products.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductModel save(ProductCreateReqDto productCreateReqDto) {
        var newProduct = productMapper.toProductModel(productCreateReqDto);

        UUID categoryId = UUID.fromString(productCreateReqDto.categoryId());
        var category = categoryService.findById(categoryId);
        newProduct.setCategory(category);

        return productRepository.save(newProduct);
    }


    public ProductModel findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found"));
    }

    public Page<ProductModel> findAllPaginated(int page, int size, String categoryId) {
        var pageable = PageRequest.of(page, size);

        if (categoryId == null) return productRepository.findAll(pageable);

        return productRepository.findByCategoryId(UUID.fromString(categoryId), pageable);
    }

    public ProductModel update(UUID id, ProductUpdateReqDto productUpdateReqDto) {
        var existingProduct = this.findById(id);

        productMapper.updateProductFromDto(productUpdateReqDto, existingProduct);

        if (productUpdateReqDto.categoryId() == null) return productRepository.save(existingProduct);

        UUID categoryId = UUID.fromString(productUpdateReqDto.categoryId());
        var category = categoryService.findById(categoryId);
        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

    public void delete(UUID id) {
        ProductModel product = this.findById(id);
        productRepository.delete(product);
    }
}
