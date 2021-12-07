package com.foodapp.backend.service;



import com.foodapp.backend.dto.request.CategoryRequest;
import com.foodapp.backend.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest categoryRequest);

    List<CategoryResponse> getListCategory();
}
