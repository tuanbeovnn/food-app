package com.foodapp.backend.config;



import com.foodapp.backend.constants.AppConstants;
import com.foodapp.backend.exceptions.MyWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new CustomTokenEnhancer();
        converter.setSigningKey("as466gf");
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.inMemory()
                .withClient(AppConstants.O2Constants.CLIEN_ID)
                .secret(encoder.encode(AppConstants.O2Constants.CLIENT_SECRET))
                .authorizedGrantTypes(AppConstants.O2Constants.GRANT_TYPE_PASSWORD, AppConstants.O2Constants.AUTHORIZATION_CODE, AppConstants.O2Constants.REFRESH_TOKEN, AppConstants.O2Constants.IMPLICIT)
                .scopes(AppConstants.O2Constants.SCOPE_READ, AppConstants.O2Constants.SCOPE_WRITE, AppConstants.O2Constants.TRUST)
//                .accessTokenValiditySeconds(AppConstant.O2Constants.ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(AppConstants.O2Constants.REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.pathMapping("/oauth/token", "/api/lms/user/login")
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .tokenEnhancer(accessTokenConverter())
                .exceptionTranslator(new MyWebResponseExceptionTranslator());
    }
}
