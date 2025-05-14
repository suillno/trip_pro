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
import org.springframework.web.servlet.ModelAndView;

import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.LoginRequest;
import com.trip.webpage.vo.MemberVO;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {
	// 서비스 객체 자동생성
	@Autowired
	private MemberService memberService;
	
// member/login, member/join
	// 로그인 연결
	@GetMapping("/login")
	// 요청이 들어오면 "index" 뷰를 반환하고, 모델(Model)에 데이터도 함께 전달하는 메서드
	public String showLoginPage(Model model) {
		model.addAttribute("userInfo", new MemberVO());
	    return "/member/login";
	}
	/**
	 * 로그인 동작 처리
	 * @param loginRequest
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/loginProc")
	public String userLoginProc(@ModelAttribute LoginRequest loginRequest,
	                            HttpSession session,
	                            Model model) {
	    log.info("로그인 요청 ID: {}, PW: {}", loginRequest.getUserId(), loginRequest.getUserPw());

	    MemberVO result = memberService.userLogin(loginRequest);

	    if (result != null) {
	        session.setAttribute("userInfo", result);
	        return "redirect:/";
	    } else {
	        model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
	        model.addAttribute("userInfo", loginRequest);  // 입력값 유지
	        return "member/login";
	    }
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
			List<MemberVO> vo = memberService.selectMember();
			log.info(vo.toString());
			return "/member/test";
		}
}
