package com.foodapp.backend.service;


import com.foodapp.backend.dto.request.PaymentRequest;
import com.foodapp.backend.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse payment(PaymentRequest request) throws Exception;
}
