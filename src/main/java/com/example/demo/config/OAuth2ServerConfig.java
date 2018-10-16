package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class OAuth2ServerConfig {
    private static final String RESOURCE_ID = "some-resource-1";
    
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Autowired
        private DataSource dataSource;
        
        @Bean
        public TokenStore jdbcTokenStore() {
            return new JdbcTokenStore(dataSource);
        }
        
        @Bean
        @Primary
        public DefaultTokenServices jdbcTokenServices() {
            final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(jdbcTokenStore());
            return defaultTokenServices;
        }
        
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources
                .resourceId(RESOURCE_ID)
                .tokenServices(jdbcTokenServices());
        }
        
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
                .antMatcher("/api/**").authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')");
        }
    }
    
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private DataSource dataSource;
        
        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;
        
        @Bean
        public TokenStore jdbcTokenStore() {
            return new JdbcTokenStore(dataSource);
        }
        
        @Bean
        public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }
        
        @Bean
        public ClientDetailsService clientDetailsService() {
            return new JdbcClientDetailsService(dataSource);
        }
        
        @Bean
        public ApprovalStore jdbcApprovalStore() {
            return new JdbcApprovalStore(dataSource);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                .tokenStore(jdbcTokenStore())
                .authorizationCodeServices(jdbcAuthorizationCodeServices())
                .approvalStore(jdbcApprovalStore())
                .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService())
                    .withClient("oauth_client")
                    .and()
                    .build();
            
            // こっちだと毎回insert走ってException発生するから、data.sqlで初期化してます。
//          clients.jdbc(dataSource)
//                  .withClient("oauth_client")
//                  .authorities("USER")
//                  .resourceIds(OAuthResourceConfig.RESOURCE_ID)
//                  .scopes("read")
//                  .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//                  .redirectUris("http://oauth-callback.com")
//                  .secret("oauth_client_secret");
        }
    }
}
