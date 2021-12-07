package com.foodapp.backend.config;

import com.foodapp.backend.constants.AppConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;


@Configuration
@EnableResourceServer
@Order(2)
public class ResourceHandleConfig extends ResourceServerConfigurerAdapter {

    public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) {
        resourceServerSecurityConfigurer.resourceId(AppConstants.ResourceServer.RESOURCE_ID).stateless(false);
    }


    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                anonymous().disable()
                .authorizeRequests()
                .antMatchers("/api/client/**").permitAll()
                .antMatchers("/api/admin/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                .antMatchers("/api/superadmin/**").hasAnyRole("SUPER_ADMIN")
//                .and().exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint());
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }


}
