package com.example.demo.domain.service.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Authorities;
import com.example.demo.domain.model.DemoUser;
import com.example.demo.domain.model.DemoUserDetails;
import com.example.demo.domain.repository.DemoUserRepository;

@Service
public class DemoUserDetailsServiceImpl implements UserDetailsService {

	private final DemoUserRepository demoUserRepository;
	
	public DemoUserDetailsServiceImpl(DemoUserRepository demoUserRepository) {
		this.demoUserRepository = demoUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		DemoUser demoUser = demoUserRepository.findByLoginId(loginId);
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(Authorities auth: demoUser.getAuthorities()){
			authorities.add(new SimpleGrantedAuthority(auth.getKey().getAuthority().toString()));
		}
		return new DemoUserDetails(demoUser.getUserId(), demoUser.getLoginId(), demoUser.getPassword(), authorities);
	}

}
