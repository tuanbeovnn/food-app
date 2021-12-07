package com.foodapp.backend.controller.categories;


import com.foodapp.backend.dto.request.CategoryRequest;
import com.foodapp.backend.dto.response.CategoryResponse;
import com.foodapp.backend.service.CategoryService;
import com.foodapp.backend.utils.ResponseEntityBuilder;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(
        tags = "Category-API",
        description = "Providing api for Category"
)
@RestController
@RequestMapping(value = "/api")
public class CategoryApi {

    @Autowired
    private CategoryService categoryService;


    @RequestMapping(value = "/admin/category/create-category", method = RequestMethod.POST)
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.create(categoryRequest);
        return ResponseEntityBuilder.getBuilder()
                .setDetails(categoryResponse)
                .setMessage("Create a category successfully")
                .build();
    }

    //    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//    public CategoryDto updateCate(@RequestBody CategoryDto categoryDto, @PathVariable("id") long id) {
//        categoryDto.setId(id);
//        return categoryService.update(categoryDto);
//    }
//
    @RequestMapping(value = "/admin/category/list", method = RequestMethod.GET)
    public ResponseEntity<?> showListCategory() {
        List<CategoryResponse> categoryResponse = categoryService.getListCategory();
        return ResponseEntityBuilder.getBuilder()
                .setDetails(categoryResponse)
                .setMessage("Get list successfully")
                .build();
    }
//
//    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
//    public void remove(@RequestBody long[] ids) {
//        categoryService.delete(ids);
//    }

}
