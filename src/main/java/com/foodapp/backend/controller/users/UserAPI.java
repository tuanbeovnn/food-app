package com.foodapp.backend.controller.users;



import com.foodapp.backend.dto.EmailDto;
import com.foodapp.backend.dto.ForgotPasswordDto;
import com.foodapp.backend.dto.UserPrincipalOauth2;
import com.foodapp.backend.dto.request.ChangePasswordRequest;
import com.foodapp.backend.dto.request.UserRequest;
import com.foodapp.backend.dto.response.UserResponse;
import com.foodapp.backend.entity.UserEntity;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.exceptions.ErrorCode;
import com.foodapp.backend.redis.ForgotPassWordRedisDto;
import com.foodapp.backend.redis.repository.ForgotPasswordRedisRepository;
import com.foodapp.backend.repository.UserRepository;
import com.foodapp.backend.service.UserService;
import com.foodapp.backend.utils.ResponseEntityBuilder;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;


/**
 * @Author: Tuan Nguyen
 */
@Api(
        tags = "User-API",
        description = "Providing api for User"
)
@RestController
@RequestMapping("/api")
public class UserAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ForgotPasswordRedisRepository forgotPasswordRedisRepository;


    @RequestMapping(value = "/admin/authentication/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody @Valid UserRequest userRequest) {
//        signUpValidationService.validate(userRequest);
        UserResponse output = userService.createUser(userRequest);
        return ResponseEntityBuilder.getBuilder().setMessage("Create User Successfully").setDetails(output).build();
    }

    @RequestMapping(value = "/authentication/forgot-password", method = RequestMethod.POST)
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDto email) throws MessagingException {
        userService.forgotPassword(email);
        return ResponseEntityBuilder.getBuilder().setMessage("Email has been sent already !").build();
    }


    @ResponseBody
    @RequestMapping(value = "/authentication/reset-password", method = RequestMethod.GET)
    public ResponseEntity<?> resetPassword(@ModelAttribute EmailDto emailModel) throws Throwable {
        ForgotPassWordRedisDto forgotPassWordRedisModel = forgotPasswordRedisRepository.findByEmail(emailModel.getEmail()).orElseThrow(() -> {
            throw new AppException(ErrorCode.ACTIVED_ACCOUNT);
        });
        userService.resetPassword(emailModel);
        forgotPasswordRedisRepository.deleteById(forgotPassWordRedisModel.getId());
        return ResponseEntityBuilder.getBuilder().setMessage("Your password has been reset and sent to your email").build();
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (userService.changePassword(changePasswordRequest)) {
            return ResponseEntityBuilder.getBuilder().setMessage("Your password had been changed successfully").build();
        } else {
            return ResponseEntityBuilder.getBuilder().setMessage("Could not change your passoword").build();
        }
    }

}
