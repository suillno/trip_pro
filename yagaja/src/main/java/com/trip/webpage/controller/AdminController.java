package com.trip.webpage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.trip.webpage.vo.SearchHelper;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	
	@Autowired
	private MemberService memberService;

	// 회원관리 페이지
	@GetMapping("/memberDetail")
	public ModelAndView memberList(
	    @RequestParam(value = "searchType", required = false) String searchType,
	    @RequestParam(value = "searchMember", required = false) String searchMember,
	    @ModelAttribute SearchHelper searchHelper,
	    HttpServletRequest request
	) {
	    ModelAndView mav = new ModelAndView("admin/memberDetail");

	    // 검색 조건 설정
	    if (searchMember != null && !searchMember.trim().isEmpty()) {
	        if ("userId".equals(searchType)) {
	            searchHelper.setUserId(searchMember);
	        } else if ("userName".equals(searchType)) {
	            searchHelper.setUserName(searchMember);
	        }
	        // 조건 기반 조회
	        log.info("검색 조건 있음: searchType={}, searchMember={}", searchType, searchMember);
	    } else {
	        log.info("검색 조건 없음, 전체 조회");
	    }

	    // 세션 사용자 정보 유지
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
	    mav.addObject("userInfo", memberVO != null ? memberVO : new MemberVO());

	    // 페이징 처리
	    int currentPage = searchHelper.getPageNumber();
	    if (currentPage < 1) currentPage = 1;

	    int pageSize = searchHelper.getPageSize();
	    if (pageSize < 1) pageSize = 10;

	    int offset = (currentPage - 1) * pageSize;
	    searchHelper.setOffset(offset);

	    log.info(searchMember);
	    // 필터링 조건에 따라 조회 수행
	    if (searchMember != null) {
	    	List<MemberVO> list = memberService.selectMemberList(searchHelper);
	    	log.info(list.toString());
	    	mav.addObject("list", list);
	    } else {
	    	List<MemberVO> list = memberService.selectMember();
	    	mav.addObject("list", list);
	    }
	    
	    int totalRecords = memberService.selectMemberCount(searchHelper);
	    int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

	    // View 데이터 전달
	    
	    mav.addObject("totalRecords", totalRecords);
	    mav.addObject("pageSize", pageSize);
	    mav.addObject("totalPages", totalPages);
	    mav.addObject("currentPage", currentPage);

	    // 검색 조건 유지
	    mav.addObject("searchType", searchType);
	    mav.addObject("searchMember", searchMember);

	    return mav;
	}

	 
	 // 회원 블락 처리 
	 @PostMapping("/blockMemberBulk")
	 public ModelAndView blockMemberBulk(@RequestParam MultiValueMap<String, String> paramMap) {
	     List<String> userIds = paramMap.get("userId");
	     List<String> blockCodes = paramMap.get("blockCode");

	     ModelAndView mav = new ModelAndView();

	     if (userIds == null || blockCodes == null || userIds.size() != blockCodes.size()) {
	         mav.setViewName("error"); // 필요 시 오류 페이지로 이동
	         return mav;
	     }

	     for (int i = 0; i < userIds.size(); i++) {
	         MemberVO vo = new MemberVO();
	         vo.setUserId(userIds.get(i));
	         vo.setBlockCode(blockCodes.get(i).charAt(0));
	         memberService.blockMember(vo);
	     }
	    
	     // ✅ 변경 후 목록 페이지로 redirect
	     mav.setViewName("redirect:/admin/memberDetail");
	     return mav;
	 }

}
