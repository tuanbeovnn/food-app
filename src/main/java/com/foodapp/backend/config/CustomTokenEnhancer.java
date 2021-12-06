package com.foodapp.backend.config;



import com.foodapp.backend.constants.AppConstant;
import com.foodapp.backend.dto.UserPrincipalOauth2;
import com.foodapp.backend.entity.UserEntity;
import com.foodapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    @Autowired
    private UserRepository userRepository;


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserPrincipalOauth2 user = (UserPrincipalOauth2) authentication.getPrincipal();
        Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
        List<String> roles = user.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        info.put("roles", roles);
        info.put("fullName", user.getUsername());
        info.put("email", user.getEmail());
        info.put("user_id", user.getUserId());
        DefaultOAuth2AccessToken customerAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customerAccessToken.setAdditionalInformation(info);
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_USER")) {
            customerAccessToken.setExpiration(new Date(System.currentTimeMillis() + ((60 * 60 * 1000)/2)));
        }else if (roles.contains("ROLE_SUPER_ADMIN")) {
            customerAccessToken.setExpiration(new Date(System.currentTimeMillis() + (AppConstant.O2Constants.ACCESS_TOKEN_VALIDITY__SUPER_MILLISECONDS * 1 * 60 * 60 * 1000)));
        }

        // set refresh token into database
        UserEntity userEntity = userRepository.findById(((UserPrincipalOauth2) authentication.getPrincipal()).getUserId()).get();
        OAuth2AccessToken result = super.enhance(customerAccessToken, authentication);
//        userEntity.setRefreshToken(result.getRefreshToken().toString());
        userRepository.save(userEntity);
        return result;
    }

}
