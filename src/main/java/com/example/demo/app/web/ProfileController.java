package com.example.demo.app.web;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "profile")
public class ProfileController {
	@GetMapping(value = "view")
	public String viewProfile(Model model, Principal principal) {
		if(principal instanceof SocialAuthenticationToken) {
			SocialAuthenticationToken saToken = (SocialAuthenticationToken)principal;
			return "redirect:/connect/"+saToken.getProviderId();
		} else if(principal instanceof UsernamePasswordAuthenticationToken) {
			
		}
		return "profile";
	}
}
