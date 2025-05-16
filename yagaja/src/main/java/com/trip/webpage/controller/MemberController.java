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
	@GetMapping("/idsearch")
	public ModelAndView showIdsearch() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("userInfo", new MemberVO());
	    return mav;
	}
	// 패스워드 찿기 연결
	@GetMapping("/password")
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
	 /**
     * 게시글 목록 페이지
     * - 검색 조건, 페이징 처리, 사용자 정보 전달
     */
    @GetMapping("/memberDetail")
    public ModelAndView boardList(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/memberDetail");

        if (searchHelper.getPageNumber() < 0) searchHelper.setPageNumber(0);

        log.info(searchHelper.toString());

        HttpSession session = request.getSession();
        MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");

        // 사용자 정보가 있을 경우 전달, 없으면 빈 객체 전달
        mav.addObject("userInfo", memberVO != null ? memberVO : new MemberVO());

        // 페이징 계산
        int currentPage = searchHelper.getPageNumber();
        if (currentPage < 1) currentPage = 1;

        int pageSize = searchHelper.getPageSize();
        if (pageSize < 1) pageSize = 10;

        int offset = (currentPage - 1) * pageSize;
        searchHelper.setOffset(offset);

        // 게시글 리스트 조회 및 페이징 정보 세팅
        List<MemberVO> list = memberService.selectList(searchHelper);
        int totalRecords = memberService.selectListCount(searchHelper);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        mav.addObject("list", list);
        mav.addObject("totalRecords", totalRecords);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.addObject("currentPage", currentPage);

        return mav;
    }
}
