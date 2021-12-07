package com.foodapp.backend.service.impl;


import com.foodapp.backend.dto.request.CategoryRequest;
import com.foodapp.backend.dto.response.CategoryResponse;
import com.foodapp.backend.entity.CategoryEntity;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.exceptions.ErrorCode;
import com.foodapp.backend.repository.CategoryRepository;
import com.foodapp.backend.service.CategoryService;
import com.foodapp.backend.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryRepository.findByCode(categoryRequest.getCode());
        if (categoryEntity != null) {
            throw new AppException(ErrorCode.CODE_EXIST);
        }
        categoryEntity = Converter.toModel(categoryRequest, CategoryEntity.class);
        categoryEntity = categoryRepository.save(categoryEntity);
        return Converter.toModel(categoryEntity, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getListCategory() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        return Converter.toList(categoryEntities, CategoryResponse.class);
    }
}
