package com.foodapp.backend.service.impl;



import com.foodapp.backend.dto.RoleDTO;
import com.foodapp.backend.dto.request.RoleRequest;
import com.foodapp.backend.dto.response.RoleResponse;
import com.foodapp.backend.entity.RoleEntity;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.exceptions.ErrorCode;
import com.foodapp.backend.repository.RoleRepository;
import com.foodapp.backend.service.RoleService;
import com.foodapp.backend.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public RoleResponse create(RoleRequest roleRequest) {
        RoleEntity roleEntity = Converter.toModel(roleRequest, RoleEntity.class);
        roleEntity = roleRepository.save(roleEntity);
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    @Transactional
    public RoleResponse update(RoleDTO roleRequest) {
        RoleEntity roleEntity = roleRepository.findById(roleRequest.getId()).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        roleEntity.setCode(roleRequest.getCode());
        roleEntity.setName(roleEntity.getName());
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    public List<RoleResponse> getListRole() {
        List<RoleEntity> lstRoles = roleRepository.findAll();
        return Converter.toList(lstRoles, RoleResponse.class);
    }
}
