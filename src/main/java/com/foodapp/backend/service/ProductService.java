package com.foodapp.backend.service;


import com.foodapp.backend.dto.request.ProductRequest;
import com.foodapp.backend.dto.response.ProductResponse;
import com.foodapp.backend.pageable.PageList;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse createProduct(MultipartFile highlights, MultipartFile[] files, ProductRequest productRequest);

    PageList<ProductResponse> findProductByCondition(String code, Pageable pageable);

    void disableProduct(Long id);

    ProductResponse findById(Long productId);

    PageList<ProductResponse> searchProduct(ProductRequest productRequest,Long categoryId, Pageable pageable);

}
