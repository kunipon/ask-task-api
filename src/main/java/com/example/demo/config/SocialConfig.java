package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import com.example.demo.domain.service.signup.ProviderImplicitSignUpService;
import com.example.demo.domain.service.signup.SignupService;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {
	
	private final DataSource dataSource;
	private final SignupService signupService;
	
	public SocialConfig(DataSource dataSource, SignupService signupService) {
		this.dataSource = dataSource;
		this.signupService = signupService;
	}
	
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(
			dataSource, connectionFactoryLocator, Encryptors.noOpText());
		repository.setConnectionSignUp(new ProviderImplicitSignUpService(signupService));
		return repository;
	}
}
