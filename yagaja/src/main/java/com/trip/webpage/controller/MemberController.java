package com.trip.webpage.controller;

import java.lang.reflect.Member;
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

import com.trip.webpage.vo.SearchHelper;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.BoardDefaultVO;
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
	 * 
	 * @param loginRequest
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/loginProc")
	public ModelAndView userLoginProc(@ModelAttribute LoginRequest loginRequest, HttpSession session) {
		log.info("로그인 요청 ID: {}, PW: {}", loginRequest.getUserId(), loginRequest.getUserPw());
		ModelAndView mav = new ModelAndView();
		MemberVO result = memberService.userLogin(loginRequest);

		if (result != null) {
			session.setAttribute("userInfo", result);
			mav.setViewName("redirect:/"); // 로그인 성공 시 메인으로 리다이렉트
		} else {
			mav.setViewName("member/login"); // 실패 시 다시 로그인 페이지로 이동
			mav.addObject("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
			mav.addObject("userInfo", loginRequest); // 입력값 유지
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

	/**
	 * 게시글 목록 페이지 - 검색 조건, 페이징 처리, 사용자 정보 전달
	 */
	@GetMapping("/memberDetail")
	public ModelAndView boardList(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("member/memberDetail");

		if (searchHelper.getPageNumber() < 0)
			searchHelper.setPageNumber(0);

		log.info(searchHelper.toString());

		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");

		// 사용자 정보가 있을 경우 전달, 없으면 빈 객체 전달
		mav.addObject("userInfo", memberVO != null ? memberVO : new MemberVO());

		// 페이징 계산
		int currentPage = searchHelper.getPageNumber();
		if (currentPage < 1)
			currentPage = 1;

		int pageSize = searchHelper.getPageSize();
		if (pageSize < 1)
			pageSize = 10;

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

	@GetMapping("/checkId")
	@ResponseBody // json 리턴시사용
	public HashMap<String, Object> checkId(@RequestParam("userId") String userId) {
		HashMap<String, Object> resultMap = new HashMap<>();
		// 05-22 이 밑에부분 로그인시 중복값 체크 [기복]
		MemberVO result = memberService.findById(userId);
		log.info(userId);
		if (result == null) {
			resultMap.put("isDuplicate", false);
		} else {
			resultMap.put("isDuplicate", true);
		}
		return resultMap;
	}

	// 회원가입 데이터 저장
	@PostMapping("/joinAction")
	@ResponseBody
	public HashMap<String, Object> joinAction(@RequestBody MemberVO memberVO) {
		log.info(memberVO.toString());
		HashMap<String, Object> map = new HashMap<>();
		memberService.insertUser(memberVO);
		map.put("result", "success");
		return map;

	}

	// 로그인 동작
	@PostMapping("/signupProc")
	public ModelAndView signupProc(@ModelAttribute MemberVO memberVO) {
		ModelAndView mav = new ModelAndView();
		log.info(memberVO.toString());
		memberService.insertUser(memberVO);
		mav.setViewName("redirect:/member/login");
		return mav;
	}

	// 정보수정 연결 05-22 기복
	@GetMapping("/profile")
	public ModelAndView profile(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");

		mav.addObject("userInfo", memberVO);
		return mav;
	}

	// 비밀번호 수정 05-22
	@PostMapping("/profileSave")
	public ModelAndView profileSave(@RequestParam("userPassword") String userPassword, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		log.info(userPassword);

		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
		memberVO.setUserPw(userPassword);
		memberService.updatePassword(memberVO);
		mav.setViewName("redirect:/");
		return mav;
	}
	
	//2025-05-22 조윤호 작성자 작성일 작업 진행 및 수정 
    @GetMapping("/detail/{id}")
    public String memberDetail(@PathVariable String id, Model model) {
        // 1. 회원 정보 조회 (이미 있음)
        MemberVO member = memberService.findById(id);
        model.addAttribute("member", member);

        // 2. 게시글 1건 조회 (해당 유저가 작성한 최근 글 등)
        BoardDefaultVO board = memberService.selectLatestByUserId(id); // 또는 selectOneByUserId 등
        if(board != null){
        // 3. 아래 코드 추가 (📌 여기!)
        model.addAttribute("author", board.getRegId());
        model.addAttribute("date", board.getReg2Date());
        }else {
        	model.addAttribute("author", "");
        }
        // 4. 뷰 이동
        return "member/detail";
    }

}
