/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import com.example.demo.domain.repository.DemoUserRepository;
import com.example.demo.domain.service.security.SocialUserDetailsServiceImpl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	@NonNull
	private final DemoUserRepository demoUserRepository;
	@NonNull
	private final UserDetailsService demoUserDetailsServiceImpl;
	
	@Autowired
	public void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(demoUserDetailsServiceImpl)
			.passwordEncoder(passwordEncoder());
	}
	
	@Configuration
	public static class DefaultSecurityConfig extends WebSecurityConfigurerAdapter{
		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
		
		@Override
		public void configure(WebSecurity web) throws Exception {
			web
				.ignoring()
					.antMatchers("/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg");
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.formLogin()
					.loginPage("/signin")
					.loginProcessingUrl("/signin/authenticate")
					.failureUrl("/signin?param.error=bad_credentials")
					.defaultSuccessUrl("/profile/view")
				.and()
					.logout()
						.logoutUrl("/signout")
						.deleteCookies("JSESSIONID")
				.and()
					.authorizeRequests()
						.antMatchers("/", "/webjars/**", "/admin/**", "/favicon.ico", "/resources/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
						.antMatchers("/**").authenticated()
				.and()
					.rememberMe()
				.and()
					.apply(new SpringSocialConfigurer()
						.postLoginUrl("/profile/view")
						.connectionAddedRedirectUrl("/profile/view")
						.signupUrl("/signup")
						.alwaysUsePostLoginUrl(true));
		}
		
	}
	
	@Bean
	public SocialUserDetailsService socialUsersDetailService() {
		return new SocialUserDetailsServiceImpl(demoUserRepository);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}
	
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

}
