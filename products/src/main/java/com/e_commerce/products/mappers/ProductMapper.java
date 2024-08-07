package com.e_commerce.products.mappers;

import com.e_commerce.products.dtos.ProductCreateReqDto;
import com.e_commerce.products.dtos.ProductUpdateReqDto;
import com.e_commerce.products.models.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductModel toProductModel(ProductCreateReqDto productCreateReqDto);

    void updateProductFromDto(ProductUpdateReqDto productUpdateReqDto, @MappingTarget ProductModel productModel);
}
