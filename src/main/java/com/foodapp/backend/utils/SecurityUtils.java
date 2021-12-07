package com.foodapp.backend.utils;


import com.foodapp.backend.dto.UserPrincipalOauth2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class SecurityUtils {


    @Autowired
    private TokenStore tokenStore;

    public static UserPrincipalOauth2 getPrincipal() {
        UserPrincipalOauth2 userPrincipalOauth2 = (UserPrincipalOauth2) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipalOauth2;
    }

    public static Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    public Map<String, Object> getAdditional(String token) {
        final OAuth2AccessToken accessToken = tokenStore.readAccessToken(token.split(" ")[1]);
        return accessToken.getAdditionalInformation();
    }

}
