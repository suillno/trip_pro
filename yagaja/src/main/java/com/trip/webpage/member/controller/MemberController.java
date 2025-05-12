package com.trip.webpage.member.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
// member/login, member/join
	
	@GetMapping("/login")
	// 요청이 들어오면 "index" 뷰를 반환하고, 모델(Model)에 데이터도 함께 전달하는 메서드
	public String showLoginPage() {

	    return "member/login";
	}

}
