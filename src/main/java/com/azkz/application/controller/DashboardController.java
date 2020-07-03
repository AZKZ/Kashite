package com.azkz.application.controller;

import java.util.List;

import com.azkz.application.resource.FriendBookDTO;
import com.azkz.businesslogic.service.BookMngService;
import com.azkz.businesslogic.service.FriendService;
import com.azkz.businesslogic.service.UserService;
import com.azkz.infrastructure.entity.ViewUserFriendList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

	private final UserService userService;
	private final FriendService friendService;
	private final BookMngService bookMngService;

	@Autowired
	public DashboardController(final UserService userService, final FriendService friendService,
			final BookMngService bookMngService) {
		this.userService = userService;
		this.friendService = friendService;
		this.bookMngService = bookMngService;
	}

	@GetMapping("/dashboard")
	public ModelAndView dashboard(ModelAndView modelAndView, @AuthenticationPrincipal OidcUser principal) {

		List<ViewUserFriendList> viewUserFriendList = friendService.getFriendList(userService.getUserId(principal));

		List<FriendBookDTO> friendBookList = bookMngService.getFriendBookList(viewUserFriendList);

		modelAndView.setViewName("dashboard");
		modelAndView.addObject("friendBookList", friendBookList);

		return modelAndView;
	}

	@GetMapping("/")
	public ModelAndView index(ModelAndView modelAndView) {
		modelAndView.setViewName("welcome");

		return modelAndView;
	}

}
