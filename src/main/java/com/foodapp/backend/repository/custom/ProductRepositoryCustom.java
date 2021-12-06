package com.foodapp.backend.repository.custom;


import com.foodapp.backend.dto.response.ProductProfileResponse;
import com.foodapp.backend.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Qualifier("ProductRepositoryCustom")
@Repository
public interface ProductRepositoryCustom {
    List<ProductEntity> findProductByCondition(Map<String, Object> params, Pageable pageable);
    Long countProductByCondition(String code);
    List<ProductProfileResponse> findListOrderByCustomerId(Long customerId);

}
