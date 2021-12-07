package com.foodapp.backend.service.impl;


import com.foodapp.backend.dto.CustomerDTO;
import com.foodapp.backend.dto.CustomerUserDTO;
import com.foodapp.backend.dto.request.CustomerRequest;
import com.foodapp.backend.dto.response.ProductProfileResponse;
import com.foodapp.backend.dto.response.ProfileResponse;
import com.foodapp.backend.entity.CustomerEntity;
import com.foodapp.backend.entity.UserEntity;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.exceptions.CustomException;
import com.foodapp.backend.exceptions.ErrorCode;
import com.foodapp.backend.repository.CustomerRepository;
import com.foodapp.backend.repository.UserRepository;
import com.foodapp.backend.repository.custom.ProductRepositoryCustom;
import com.foodapp.backend.service.CustomerService;
import com.foodapp.backend.service.OrderRepository;
import com.foodapp.backend.utils.Converter;
import com.foodapp.backend.utils.SecurityUtils;
import com.foodapp.backend.utils.StringConvert;
import com.foodapp.backend.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.foodapp.backend.constants.AppConstants.ACTIVE.ACTIVE_STATUS;
import static com.foodapp.backend.constants.AppConstants.ACTIVE.INACTIVE_STATUS;

@Service
public class CustomerImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerImpl(CustomerRepository customerRepository,
                        UserRepository userRepository,
                        OrderRepository orderRepository,
                        ProductRepositoryCustom productRepositoryCustom,
                        PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepositoryCustom = productRepositoryCustom;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerRequest customerRequest) {
        UserEntity user = saveOrUpdateUserForEmployee(customerRequest.getUser());
        CustomerEntity customerEntity = Converter.toModel(customerRequest, CustomerEntity.class);
        customerEntity.setUser(user);
        customerEntity.setEmail(user.getEmail());
        customerEntity = customerRepository.save(customerEntity);

        return Converter.toModel(customerEntity, CustomerDTO.class);
    }

    @Override
    public ProfileResponse userProfile() {
        ProfileResponse profileResponse = new ProfileResponse();
        CustomerEntity customerEntity = customerRepository.findById(SecurityUtils.getPrincipal().getUserId())
                .orElseThrow(() -> new CustomException("Not found user by id " + SecurityUtils.getPrincipal().getUserId()));

        Long countOrderCustomer = orderRepository.countByCustomerId(SecurityUtils.getPrincipal().getUserId());
        List<ProductProfileResponse> productProfileResponses = productRepositoryCustom.findListOrderByCustomerId(SecurityUtils.getPrincipal().getUserId());
        profileResponse.setListProduct(productProfileResponses);
        profileResponse.setOrder(countOrderCustomer);
        profileResponse.setPending(INACTIVE_STATUS);
        profileResponse.setName(customerEntity.getUserName());

        return profileResponse;
    }

    private UserEntity saveOrUpdateUserForEmployee(CustomerUserDTO user) {
        Map<String, Object> validateResult = ValidationUtils.validateRequired(user);
        if (!validateResult.isEmpty()) {
            throw new AppException(ErrorCode.EMPTY_PRODUCT);
        }
        validateUserCreate(user.getUsername(), user.getEmail());
        UserEntity entity = Converter.toModel(user, UserEntity.class);
        entity.setFullName(StringConvert.convertUpperCaseStringName(user.getFullName()));
        entity.setStatus(ACTIVE_STATUS);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(entity);
    }

    private void validateUserCreate(String username, String email) {
        UserEntity userValid = userRepository.findOneByEmailOrUserNameAndStatus(email, username, ACTIVE_STATUS);
        if (userValid != null) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
    }
}
