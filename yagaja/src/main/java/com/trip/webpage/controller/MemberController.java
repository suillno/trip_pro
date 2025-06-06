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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trip.webpage.vo.SearchHelper;
import com.trip.webpage.repository.PasswordResetTokenRepository;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.service.PasswordResetService;
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

	// 비밀번호 이메일 객체 자동생성(?) 05-28
	@Autowired
	private PasswordResetTokenRepository tokenRepository;
	@Autowired
	private PasswordResetService passwordResetService;

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
	        if ('Y' == result.getBlockCode()) {
	            // 차단된 사용자
	            mav.setViewName("member/login");
	            mav.addObject("error", "해당 계정은 관리자에 의해 차단되었습니다.");
	            mav.addObject("userInfo", loginRequest);
	        } else {
	            session.setAttribute("userInfo", result);
	            memberService.saveVisit(result.getUserId());
	            mav.setViewName("redirect:/"); // 메인 페이지로 리다이렉트
	        }
	    } else {
	        mav.setViewName("member/login"); // 로그인 실패
	        mav.addObject("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
	        mav.addObject("userInfo", loginRequest);
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

		if (memberVO != null) {
	        String originalEmail = memberVO.getEmail();
	        String maskedEmail = maskEmail(originalEmail);
	        mav.addObject("userInfo", memberVO);
	        mav.addObject("maskedEmail", maskedEmail);
	    }
		
		return mav;
	}

	// 0528 오후 수정
	@PostMapping("/profileSave")
	public ModelAndView profileSave(@RequestParam("userPassword") String userPassword, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mav = new ModelAndView();
		log.info(userPassword);

		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
		memberVO.setUserPw(userPassword);
		memberService.updatePassword(memberVO);
		redirectAttributes.addFlashAttribute("passwordChanged", true);
		mav.setViewName("redirect:/member/profile");
		return mav;

		// 여기까지 수정 0528 오후
	}

	// 2025-05-22 조윤호 작성자 작성일 작업 진행 및 수정
	@GetMapping("/detail/{id}")
	public String memberDetail(@PathVariable String id, Model model) {
		// 1. 회원 정보 조회 (이미 있음)
		MemberVO member = memberService.findById(id);
		model.addAttribute("member", member);

		// 2. 게시글 1건 조회 (해당 유저가 작성한 최근 글 등)
		BoardDefaultVO board = memberService.selectLatestByUserId(id); // 또는 selectOneByUserId 등
		if (board != null) {
			// 3. 아래 코드 추가 (📌 여기!)
			model.addAttribute("author", board.getRegId());
			model.addAttribute("date", board.getReg2Date());
		} else {
			model.addAttribute("author", "");
		}
		// 4. 뷰 이동
		return "member/detail";
	}

	// 회원탈퇴 연결
	@RequestMapping("memberShip")
	public ModelAndView memberShip(HttpServletRequest request, @ModelAttribute("error") String error) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");

		if (memberVO != null) {
	        String originalEmail = memberVO.getEmail();
	        String maskedEmail = maskEmail(originalEmail);
	        mav.addObject("userInfo", memberVO);
	        mav.addObject("maskedEmail", maskedEmail);
	    }
		
		mav.addObject("error", error);
		return mav;
	}

	// 회원 탈퇴
	@PostMapping("/shipSave")
	public String memberSave(@RequestParam String userPassword, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		ModelAndView mav = new ModelAndView();

		boolean pwMatch = memberService.checkPasswordForWithdraw(userPassword, request);
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");

		if (pwMatch) {
			memberService.updateShip(memberVO);
			return "redirect:/member/logout";
		} else {
			redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
			return "redirect:/member/memberShip";
		}

	}

	@PostMapping("/findId")
	public ModelAndView findID(@ModelAttribute MemberVO memberVO) {
		ModelAndView mav = new ModelAndView("/member/idsearch");

		MemberVO vo = memberService.findUserId(memberVO);
		log.info("조회 결과: {}", vo);

		if (vo != null && vo.getUserId() != null) {
			mav.addObject("userId", vo.getUserId()); // JS에서 쓸 변수명과 맞춤
		} else {
			mav.addObject("userId", ""); // 찾지 못한 경우
		}

		mav.addObject("isFirst", true);

		return mav;

	}

	// 0528 5시 30분 여기 밑으로 다시 싹다 복붙
	@PostMapping("/reset-password")
	public String processResetPassword(@ModelAttribute MemberVO memberVO, RedirectAttributes redirectAttributes) {
		MemberVO member = memberService.findByEmail(memberVO);

		if (member == null) {
			redirectAttributes.addFlashAttribute("error", "해당 이메일로 등록된 계정이 없습니다.");
			return "redirect:/member/passwordSearch";

		} else {
			log.info("없다");
		}

		// 랜덤 비밀번호 생성
		String tempPassword = generateRandomPassword();

		// 비밀번호 암호화 (선택사항)
		member.setUserPw(tempPassword); // 여기서 암호화 해도 좋음

		// DB에 비밀번호 저장
		memberService.updatePassword(member);
		//
		// 이메일로 임시 비밀번호 전송
		memberService.sendTempPassword(member.getEmail(), tempPassword);

		redirectAttributes.addFlashAttribute("message", true);

		return "redirect:/member/passwordSearch"; // 로그인 화면으로 이동
	}

	private String generateRandomPassword() {
		int length = 8;
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
		StringBuilder password = new StringBuilder();
		java.util.Random random = new java.util.Random();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(chars.length());
			password.append(chars.charAt(index));
		}

		return password.toString();
	}

	// 이메일 마스킹 유틸 메서드
	private String maskEmail(String email) {
	    if (email == null || !email.contains("@")) return email;
	    String[] parts = email.split("@");
	    String name = parts[0];
	    String domain = parts[1];

	    if (name.length() <= 3) {
	        return name + "@" + domain;
	    }

	    String visible = name.substring(0, 3);
	    String masked = "*".repeat(name.length() - 3);
	    return visible + masked + "@" + domain;
	}
}
