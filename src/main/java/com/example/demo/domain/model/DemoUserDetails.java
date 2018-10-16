package com.example.demo.domain.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoUserDetails extends User {
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	public DemoUserDetails(String userId
						  ,String loginId
						  ,String password
						  ,Collection<? extends GrantedAuthority> authorities) {
		super(loginId, password, authorities);
		this.userId = userId;
	}
}
