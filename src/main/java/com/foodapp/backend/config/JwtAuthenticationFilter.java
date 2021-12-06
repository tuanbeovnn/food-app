package com.foodapp.backend.config;


import com.foodapp.backend.constants.AppConstant;
import com.foodapp.backend.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(AppConstant.O2Constants.HEADER_STRING);
        String email = null;
        if (header != null && header.startsWith(AppConstant.O2Constants.TOKEN_PREFIX)) {
            email = jwtTokenUtil.getEmailFromToken();
        }

        if (email != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(userDetails.getAuthorities(), userDetails);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }
}
