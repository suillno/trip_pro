package com.trip.webpage.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trip.webpage.service.BoardService;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.util.StringUtil;
import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.BoardVO;
import com.trip.webpage.vo.MemberVO;
import com.trip.webpage.vo.SearchHelper;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {
	
	private final BoardService boardService;
	private final MemberService memberService;

	    @GetMapping("/list")
	    public ModelAndView noticePage(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
	    	ModelAndView mav = new ModelAndView();
	        mav.setViewName("board/notice");

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
	    
	    @RequestMapping("/write")
		public ModelAndView boardWrite(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
			ModelAndView mav = new ModelAndView();
			log.info(searchHelper.toString());
			
			HttpSession session = request.getSession();
			MemberVO empVO = (MemberVO) session.getAttribute("userInfo");
			mav.addObject("userInfo", empVO != null ? empVO : new MemberVO());
			mav.addObject("searchHelper", searchHelper);
			mav.addObject("result", new BoardDefaultVO());
			mav.setViewName("board/write");
			return mav;
		}
	    
	    @PostMapping("/save")
	    public ModelAndView boardSave(
	            @ModelAttribute SearchHelper searchHelper, 
	            @ModelAttribute BoardDefaultVO boardDefaultVO,
	            HttpServletRequest request) {

	        ModelAndView mav = new ModelAndView();
	        log.info(searchHelper.toString());

	        HttpSession session = request.getSession();
	        MemberVO vo = (MemberVO) session.getAttribute("userInfo");

	        // 작성자 정보 설정
	        boardDefaultVO.setRegId(vo.getUserId());
	        boardDefaultVO.setUpdateId(vo.getUserId());
	        boardDefaultVO.setUserId(vo.getUserId());
	        
	        // 날짜 설정 (현재 시각)
	        boardDefaultVO.setReg2Date(LocalDateTime.now());
	        boardDefaultVO.setUpdateDate(LocalDateTime.now());

	        // 게시글 저장 or 수정
	        if (boardDefaultVO.getBodIdx() == null) {
	            boardService.insertBoard(boardDefaultVO);
	        } else {
	            boardService.updateBoard(boardDefaultVO);
	        }

	        // redirect URL 설정
	        String url = StringUtil.searchString("/board/view", searchHelper);
	        mav.setViewName("redirect:" + url + "&boardNo=" + boardDefaultVO.getBodIdx());
	        return mav;
	    }

	    
	    @GetMapping("/view")
	    public ModelAndView boardView(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
	    	log.info(searchHelper.toString());
	    	ModelAndView mav = new ModelAndView();
	    	
	    	HttpSession session = request.getSession();
			MemberVO vo = (MemberVO) session.getAttribute("userInfo");
			
			if (vo != null) {
				mav.addObject("userInfo", vo);
			} else {
				mav.addObject("userInfo", new MemberVO());
			}
			
			mav.addObject("info", new BoardVO());
			
	    	mav.setViewName("board/view");
	    	return mav;
	    }
	    
	  
	    @GetMapping("/review")
	    public ModelAndView reviewPage(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
	    	ModelAndView mav = new ModelAndView();
	        mav.setViewName("board/review");

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
	        List<BoardVO> list = boardService.selectList(searchHelper);
	        int totalRecords = boardService.selectListCount(searchHelper);
	        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

	        mav.addObject("list", list);
	        mav.addObject("totalRecords", totalRecords);
	        mav.addObject("pageSize", pageSize);
	        mav.addObject("totalPages", totalPages);
	        mav.addObject("currentPage", currentPage);

	        return mav;
	    }
	    
	  

	    @GetMapping("/tripshare")
	    public ModelAndView tripsharePage(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
	    	ModelAndView mav = new ModelAndView();
	        mav.setViewName("board/tripshare");

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
	        List<BoardVO> list = boardService.selectList(searchHelper);
	        int totalRecords = boardService.selectListCount(searchHelper);
	        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

	        mav.addObject("list", list);
	        mav.addObject("totalRecords", totalRecords);
	        mav.addObject("pageSize", pageSize);
	        mav.addObject("totalPages", totalPages);
	        mav.addObject("currentPage", currentPage);

	        return mav;
	    }
	    
	    @GetMapping("/free")
	    public ModelAndView freePage(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
	    	ModelAndView mav = new ModelAndView();
	        mav.setViewName("board/free");

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
	        List<BoardVO> list = boardService.selectList(searchHelper);
	        int totalRecords = boardService.selectListCount(searchHelper);
	        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

	        mav.addObject("list", list);
	        mav.addObject("totalRecords", totalRecords);
	        mav.addObject("pageSize", pageSize);
	        mav.addObject("totalPages", totalPages);
	        mav.addObject("currentPage", currentPage);

	        return mav;
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
