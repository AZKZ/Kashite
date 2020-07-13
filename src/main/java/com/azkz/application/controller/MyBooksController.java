package com.azkz.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.azkz.application.resource.BookInfoForm;
import com.azkz.businesslogic.service.BookAPIService;
import com.azkz.businesslogic.service.BookMngService;
import com.azkz.businesslogic.service.UserService;

@Controller
public class MyBooksController {

	private final BookAPIService bookAPIService;

	private final BookMngService bookMngService;

	private final UserService userService;

	@Autowired
	public MyBooksController(BookAPIService bookAPIService, BookMngService bookMngService, UserService userService) {
		this.bookAPIService = bookAPIService;
		this.bookMngService = bookMngService;
		this.userService = userService;
	}

	@GetMapping("/addbookform")
	public ModelAndView directAddBookForm(ModelAndView modelAndView,
			@RequestParam(name = "isbn", required = false) String isbn) {

		BookInfoForm bookInfoForm = bookAPIService.getBookInfo(isbn);

		modelAndView.setViewName("bookform");
		modelAndView.addObject("bookInfoForm", bookInfoForm);

		return modelAndView;
	}

	@PostMapping("/addbook")
	public String addBook(@ModelAttribute("bookInfoForm") BookInfoForm bookInfoForm,
			@AuthenticationPrincipal OidcUser principal) {

		bookMngService.add(bookInfoForm, userService.getUserId(principal));

		return "redirect:/booklist";
	}

	@GetMapping("/booklist")
	public ModelAndView directBookListPage(ModelAndView modelAndView, @AuthenticationPrincipal OidcUser principal) {

		modelAndView.setViewName("booklist");
		modelAndView.addObject("viewUserBookList", bookMngService.getUserBookList(userService.getUserId(principal)));
		return modelAndView;
	}

	@ResponseBody
	@PostMapping("/deleteBook")
	public String deleteBook(@AuthenticationPrincipal OidcUser principal,
			@RequestParam("idCheckbox") List<Long> tubIdList) {

		int deleteCnt = bookMngService.modifyUserBook(tubIdList, userService.getUserId(principal),
				com.azkz.common.ModifyModeEnum.DELETE, '-');

		String alertMessage = deleteCnt + "件削除しました。";
		return alertMessage;
	}

	@ResponseBody
	@PostMapping("/updateBook")
	public String updateBook(@AuthenticationPrincipal OidcUser principal,
			@RequestParam("idCheckbox") List<Long> tubIdList, @RequestParam("statusCode") char statusCode) {
		int updateCnt = bookMngService.modifyUserBook(tubIdList, userService.getUserId(principal),
				com.azkz.common.ModifyModeEnum.UPDATE, statusCode);
		String alertMessage = updateCnt + "件更新しました。";
		return alertMessage;
	}

}
