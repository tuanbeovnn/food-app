package com.foodapp.backend.service.impl;



import com.foodapp.backend.dto.EmailDto;
import com.foodapp.backend.dto.ForgotPasswordDto;
import com.foodapp.backend.dto.request.ChangePasswordRequest;
import com.foodapp.backend.dto.request.UserRequest;
import com.foodapp.backend.dto.response.UserResponse;
import com.foodapp.backend.entity.RoleEntity;
import com.foodapp.backend.entity.UserEntity;
import com.foodapp.backend.exceptions.AppException;
import com.foodapp.backend.exceptions.CustomException;
import com.foodapp.backend.exceptions.ErrorCode;
import com.foodapp.backend.redis.ForgotPassWordRedisDto;
import com.foodapp.backend.redis.repository.ForgotPasswordRedisRepository;
import com.foodapp.backend.repository.RoleRepository;
import com.foodapp.backend.repository.UserRepository;
import com.foodapp.backend.service.UserService;
import com.foodapp.backend.utils.Converter;
import com.foodapp.backend.utils.SecurityUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;
import static com.foodapp.backend.constants.AppConstants.ACTIVE.ACTIVE_STATUS;

@Service
public class UserServiceImpl implements UserService {

    @Value("${jwt.secret}")
    private String secret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailServiceUtil emailServiceUtil;
    private final ForgotPasswordRedisRepository forgotPasswordRedisRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           EmailServiceUtil emailServiceUtil,
                           ForgotPasswordRedisRepository forgotPasswordRedisRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailServiceUtil = emailServiceUtil;
        this.forgotPasswordRedisRepository = forgotPasswordRedisRepository;
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        UserEntity userEntity = userRepository.findOneByUserName(userRequest.getUserName());
        if (null != userEntity) {
            throw new CustomException("Existing user by user name " + userRequest.getUserName());
        }

        List<RoleEntity> roleEntities = roleRepository.findByIdIn(userRequest.getRoleIds());
        if (CollectionUtils.isEmpty(roleEntities)) {
            throw new CustomException("Not found any roles in list " + userRequest.getRoleIds().toString());
        }

        userEntity = Converter.toModel(userRequest, UserEntity.class);
        userEntity.setRoles(roleEntities);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setStatus(ACTIVE_STATUS);
        userEntity = userRepository.save(userEntity);

        return Converter.toModel(userEntity, UserResponse.class);
    }

    @Override
    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        long expire = System.currentTimeMillis() + 20 * 60 * 1000;
        String signature = DigestUtils.sha256Hex(forgotPasswordDto.getEmail() + expire + this.secret);
        emailServiceUtil.sendEmailActiveCode(forgotPasswordDto.getEmail(), expire, signature);
        ForgotPassWordRedisDto forgotPassWordRedisDto = new ForgotPassWordRedisDto();
        forgotPassWordRedisDto.setEmail(forgotPasswordDto.getEmail());
        forgotPassWordRedisDto.setStatus(ACTIVE_STATUS);
        forgotPasswordRedisRepository.save(forgotPassWordRedisDto);
    }

    @Override
    @Transactional
    public void resetPassword(EmailDto emailModel) {
        try {
            if (!DigestUtils.sha256Hex(emailModel.getEmail() + emailModel.getExpire() + this.secret).equals(emailModel.getSignature())) {
                throw new AppException(ErrorCode.SIGNATURE_NOT_CORRECT);
            }
            if (System.currentTimeMillis() > emailModel.getExpire()) {
                throw new AppException(ErrorCode.EXPIRED);
            }
            UserEntity userEntity = userRepository.findOneByEmailOrUserNameAndStatus(emailModel.getEmail(), emailModel.getEmail(), ACTIVE_STATUS);
            if (userEntity == null) {
                throw new AppException(ErrorCode.ID_NOT_FOUND);
            }
            String newPassword = UUID.randomUUID().toString();
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
            emailServiceUtil.sendMail(emailModel.getEmail(), "RESET PASSWORD", "Current Password: " + newPassword);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public boolean changePassword(ChangePasswordRequest changePasswordRequest) {

        UserEntity userEntity = userRepository.findById(SecurityUtils.getPrincipal().getUserId())
                .orElseThrow((() -> new AppException(ErrorCode.ID_NOT_FOUND)));

        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(userEntity);
            return true;
        }

        throw new AppException(ErrorCode.PASSWORD_DID_NOT_MATCH);
    }
}
