package com.foodapp.backend.service;



import com.foodapp.backend.dto.ApiDTO;
import com.foodapp.backend.dto.request.ApiRequest;
import com.foodapp.backend.pageable.PageList;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ApiService {
    ApiDTO createApiSystem(ApiRequest input);
    ApiDTO updateApiSystem(ApiDTO update);
    void delete(List<Long> ids);
    PageList<ApiDTO> getAll(Pageable pageable);
    ApiDTO findOne(Long id);
    void deleteById(Long id);
}
