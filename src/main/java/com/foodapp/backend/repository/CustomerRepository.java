package com.foodapp.backend.repository;


import com.foodapp.backend.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    CustomerEntity findOneByUserName(String userName);
    CustomerEntity findOneByEmailOrUserName(String email, String username);
}