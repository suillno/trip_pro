package com.trip.webpage.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {
	// 서비스 객체 자동생성
	@Autowired
	private MemberService memberService;
	
// member/login, member/join
	// 로그인 연결
	@GetMapping("/login")
	// 요청이 들어오면 "index" 뷰를 반환하고, 모델(Model)에 데이터도 함께 전달하는 메서드
	public String showLoginPage() {

	    return "/member/login";
	}
	// 아이디 찿기 연결
	@GetMapping("/idsearch")
	public String showIdsearch() {

	    return "/member/idsearch";
	}
	// 패스워드 찿기 연결
	@GetMapping("/password")
	public String showPasswordSearch() {
		return "/member/passwordSearch";
	}
	// 회원가입 연결
	@GetMapping("/signup")
	public String showSigup() {
		
		 return "/member/signup";
	}
	// sql 데이터 연결 테스트
	@GetMapping("/test") 
		public String showTest() {
			List<MemberVO> vo = memberService.seletMember();
			return "/member/test";
		}
}
