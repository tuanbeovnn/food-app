package com.foodapp.backend.redis.repository;



import com.foodapp.backend.redis.ForgotPassWordRedisDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRedisRepository extends CrudRepository<ForgotPassWordRedisDto, Long> {
    Optional<ForgotPassWordRedisDto> findByEmail(String email);
    void deleteByEmail(String email);
}
