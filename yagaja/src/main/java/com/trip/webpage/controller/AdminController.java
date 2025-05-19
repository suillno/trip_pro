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

import com.example.demo.vo.SearchHelper;
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
	    public ModelAndView memberList(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
	        ModelAndView mav = new ModelAndView();
	        mav.setViewName("admin/memberDetail");

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

	        // 회원정보 조회 및 페이지
	        List<MemberVO> list = memberService.selectMember();
	        int totalRecords = memberService.selectMemberCount(searchHelper);
	        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

	        mav.addObject("list", list);
	        mav.addObject("totalRecords", totalRecords);
	        mav.addObject("pageSize", pageSize);
	        mav.addObject("totalPages", totalPages);
	        mav.addObject("currentPage", currentPage);

	        return mav;
	    }
	 
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
