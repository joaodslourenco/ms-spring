package com.e_commerce.products.mappers;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
import com.e_commerce.products.dtos.CategoryUpdateReqDto;
import com.e_commerce.products.models.CategoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryModel toCategoryModel(CategoryCreateReqDto categoryCreateReqDto);

    void updateCategoryFromDto(CategoryUpdateReqDto updateReqDto, @MappingTarget CategoryModel categoryModel);
}
