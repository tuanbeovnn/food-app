package com.foodapp.backend.service;





import com.foodapp.backend.dto.RoleDTO;
import com.foodapp.backend.dto.request.RoleRequest;
import com.foodapp.backend.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest roleRequest);
    RoleResponse update(RoleDTO roleRequest);
    List<RoleResponse> getListRole();
}
