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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.vo.BoardVO;
import com.example.demo.vo.EmpVO;
import com.example.demo.vo.SearchHelper;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.LoginRequest;
import com.trip.webpage.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
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
	public ModelAndView showLoginPage() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("userInfo", new MemberVO());
	    return mav;
	}
	/**
	 * 로그인 동작 처리
	 * @param loginRequest
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/loginProc")
	public ModelAndView userLoginProc(@ModelAttribute LoginRequest loginRequest,
	                                  HttpSession session) {
	    log.info("로그인 요청 ID: {}, PW: {}", loginRequest.getUserId(), loginRequest.getUserPw());
	    ModelAndView mav = new ModelAndView();
	    MemberVO result = memberService.userLogin(loginRequest);
	    
	    if (result != null) {
	        session.setAttribute("userInfo", result);
	        mav.setViewName("redirect:/");  // 로그인 성공 시 메인으로 리다이렉트
	    } else {
	        mav.setViewName("member/login");  // 실패 시 다시 로그인 페이지로 이동
	        mav.addObject("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
	        mav.addObject("userInfo", loginRequest);  // 입력값 유지
	    }
	    return mav;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate(); // 세션 초기화
	    return "redirect:/";
	}
	
	// 아이디 찿기 연결
	@RequestMapping("/idsearch")
	public ModelAndView showIdsearch() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("userInfo", new MemberVO());
		mav.addObject("userId", "");
	    return mav;
	}
	// 패스워드 찿기 연결
	@GetMapping("/passwordSearch")
	public ModelAndView showPasswordSearch() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("userInfo", new MemberVO());
	    return mav;
	}
	// 회원가입 연결
	@GetMapping("/signup")
	public ModelAndView showSigup() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("userInfo", new MemberVO());
	    return mav;
	}
    
	@GetMapping("/checkId")
	@ResponseBody
	public HashMap<String, Object> checkId (@RequestParam ("userId") String userId){
		HashMap<String, Object> resultMap = new HashMap<>();
		log.info(userId);
		resultMap.put("isDuplicate", true);
		return resultMap;
	}
	
	@PostMapping("/joinAction")
	@ResponseBody
	public HashMap<String, Object> joinAction (
			@RequestBody MemberVO memberVO
			) {
		log.info(memberVO.toString());
		HashMap<String, Object> map = new HashMap<>();
		memberService.insertUser(memberVO);
		map.put("result", "success");
		return map;
		
	}
	
	@PostMapping("/signupProc") 
	public ModelAndView signupProc(@ModelAttribute MemberVO memberVO) {
		ModelAndView mav = new ModelAndView();
		log.info(memberVO.toString());
		memberService.insertUser(memberVO);
		mav.setViewName("redirect:/member/login");
		return mav;
	}
	
	// 아이디 찾기
	@PostMapping("/findId")
	public ModelAndView findId(@ModelAttribute MemberVO memberVO) {

	    MemberVO result = memberService.findUserId(memberVO);
	    log.info("아이디 찾기 결과: {}", result != null ? result.toString() : "일치하는 회원 없음");

	    ModelAndView mav = new ModelAndView();

	    if (result != null) {
	        mav.addObject("userId", result.getUserId());   // alert용
	        mav.addObject("found", true);                  // 성공 여부 표시
	    } else {
	        mav.addObject("userId", null);
	        mav.addObject("found", false);                 // 실패 표시
	    }

	    mav.setViewName("member/idsearch");
	    return mav;
	}

}