package com.foodapp.backend.service;



import com.foodapp.backend.dto.EmailDto;
import com.foodapp.backend.dto.ForgotPasswordDto;
import com.foodapp.backend.dto.request.ChangePasswordRequest;
import com.foodapp.backend.dto.request.UserRequest;
import com.foodapp.backend.dto.response.UserResponse;

import javax.mail.MessagingException;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    void forgotPassword(ForgotPasswordDto forgotPasswordDto);
    void resetPassword(EmailDto emailModel);
    boolean changePassword(ChangePasswordRequest changePasswordRequest);
}
