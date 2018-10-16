package com.example.demo.app.web.signup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.domain.model.DemoUser;
import com.example.demo.domain.service.signup.SignupService;

/**
 * ユーザーによる明示的なサインアップ
 * 
 * 
 * @author k_kawasaki
 */
@Controller
public class SignupController {
	
	private final SignupService signupService;
	
	public SignupController( SignupService signupService) {
		this.signupService = signupService;
	}
	
	@GetMapping(value="/signup")
	public String signup(SignupForm signupForm) {
		return "signup";
	}
	
	@PostMapping(value="signup")
	public String signup(SignupForm signupForm, HttpServletRequest request) throws ServletException {
		
		DemoUser demoUser = signupService.createDemoUser(
				 signupForm.getLoginId()
				,signupForm.getPassword()
				,signupForm.getFirstName()
				,signupForm.getLastName());
		
		if(demoUser != null) {
			request.login(signupForm.getLoginId(), signupForm.getPassword());
			return "redirect:/profile/view";
		}
		
		// TODO: error handling.
		return null;
	}
}
