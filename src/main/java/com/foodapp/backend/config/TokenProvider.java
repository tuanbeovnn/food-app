package com.foodapp.backend.config;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.backend.constants.AppConstants;
import com.foodapp.backend.utils.SecurityUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class TokenProvider {
    public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 24 * 30;
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final static String SECRET = "javanuise";

    UsernamePasswordAuthenticationToken getAuthentication(Collection<? extends GrantedAuthority> authorities, final UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public String generateToken(UserDetails userDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claims = objectMapper.convertValue(userDetails, Map.class);
        return doGenerateToken(claims, userDetails.getUsername());
    }
    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

}
