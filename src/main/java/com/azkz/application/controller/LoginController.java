package com.azkz.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.azkz.businesslogic.service.UserService;

@Controller
public class LoginController {

	private final UserService userService;

	@Autowired
	public LoginController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login_success")
	public String login(@AuthenticationPrincipal OidcUser principal) {

		userService.initialLogin(principal);

		return "redirect:/dashboard";

	}

}
