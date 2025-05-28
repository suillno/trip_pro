package com.trip.webpage.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.trip.webpage.service.BoardService;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.util.StringUtil;
import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.CommentVO;
import com.trip.webpage.vo.MemberVO;
import com.trip.webpage.vo.SearchHelper;

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
	public ModelAndView listPage(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		// 카테고리에 따라 동적 View 설정
		String viewName = switch (searchHelper.getCate() != null ? searchHelper.getCate().intValue() : 1000) {
		case 1000 -> "board/notice";
		case 2000 -> "board/review";
		case 3000 -> "board/tripshare";
		case 4000 -> "board/free";
		default -> "board/notice";
		};
		mav.setViewName(viewName);

		// 검색 조건 설정
		if (searchHelper.getSearchType() != null && !searchHelper.getSearchType().trim().isEmpty()) {
			if ("userId".equals(searchHelper.getSearchType())) {
				searchHelper.setUserId(searchHelper.getSearchKeyword());
			} else if ("bodTitle".equals(searchHelper.getSearchType())) {
				searchHelper.setBodTitle(searchHelper.getSearchKeyword());
			}
			log.info("검색 조건 있음: searchType={}, searchKeyword={}", searchHelper.getSearchType(),
					searchHelper.getSearchKeyword());
		} else {
			log.info("검색 조건 없음, 전체 조회");
		}

		// 페이징 계산
		setPagingData(searchHelper, mav);

		log.info(searchHelper.toString());

		// 세션 사용자 정보 유지
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
		mav.addObject("userInfo", memberVO != null ? memberVO : new MemberVO());

		mav.addObject("cate", searchHelper.getCate());
		return mav;
	}

	@RequestMapping("/write")
	public ModelAndView boardWrite(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("board/write");

		if (searchHelper.getBodIdx() != null) {
			mav.addObject("result", boardService.selectOne(searchHelper.getBodIdx()));
		} else {
			mav.addObject("result", new BoardDefaultVO());
		}

		HttpSession session = request.getSession();
		MemberVO empVO = (MemberVO) session.getAttribute("userInfo");
		mav.addObject("userInfo", empVO != null ? empVO : new MemberVO());
		mav.addObject("searchHelper", searchHelper);
		return mav;
	}

	@PostMapping("/save")
	public ModelAndView boardSave(@ModelAttribute SearchHelper searchHelper,
			@ModelAttribute BoardDefaultVO boardDefaultVO, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();
		MemberVO vo = (MemberVO) session.getAttribute("userInfo");

		// 2025-05-26 조윤호 널 관련 오류 수정중
		if (vo == null) {
			// 로그인 안 된 사용자일 경우 처리
			// 게시판 리디렉션
			return new ModelAndView("redirect:/board/list?cate=1000&pageNumber=1");
		}

		// 2025-02-26 조윤호 boardDefaultVO.setRegId(vo.getUserId()); 추가
		boardDefaultVO.setUserId(vo.getUserId());
		boardDefaultVO.setRegId(vo.getUserId());
		boardDefaultVO.setReg2Date(LocalDateTime.now());
		boardDefaultVO.setUpdateDate(LocalDateTime.now());

		log.info("save 테스트 : ", searchHelper.toString());

		if (boardDefaultVO.getBodIdx() == null) {
			boardService.insertBoard(boardDefaultVO);
			log.info("글 저장 성공: {}", boardDefaultVO);
		} else {
			boardService.updateBoard(boardDefaultVO);
			log.info("글 수정 성공: {}", boardDefaultVO);
		}

		// 2025-02-26 조윤호 Bodno -> bodIDX 로 바꿈
		String url = StringUtil.searchString("/board/view", searchHelper);
		return new ModelAndView(
				"redirect:" + url + "&bodIdx=" + boardDefaultVO.getBodIdx() + "&cate=" + searchHelper.getCate());
	}

	@GetMapping("/view")
	public ModelAndView boardView(@ModelAttribute SearchHelper searchHelper, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("board/view");
		HttpSession session = request.getSession();
		MemberVO vo = (MemberVO) session.getAttribute("userInfo");

		// 댓글 불러오기
		List<CommentVO> commentVO = boardService.selectCommentList(searchHelper.getBodIdx());
		mav.addObject("commentList", commentVO != null ? commentVO : new CommentVO());

		mav.addObject("userInfo", vo != null ? vo : new MemberVO());
		mav.addObject("searchHelper", searchHelper);
		mav.addObject("info", boardService.selectOne(searchHelper.getBodIdx()));
		return mav;
	}

	private void setPagingData(SearchHelper searchHelper, ModelAndView mav) {
		int currentPage = Math.max(searchHelper.getPageNumber(), 1);
		int pageSize = Math.max(searchHelper.getPageSize(), 10);
		int offset = (currentPage - 1) * pageSize;

		searchHelper.setOffset(offset);

		List<BoardDefaultVO> list = boardService.selectList(searchHelper);
		int totalRecords = boardService.selectListCount(searchHelper);
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

		mav.addObject("list", list);
		mav.addObject("totalRecords", totalRecords);
		mav.addObject("pageSize", pageSize);
		mav.addObject("totalPages", totalPages == 0 ? 1 : totalPages);
		mav.addObject("currentPage", currentPage);
	}

	// 2025-05-27삭제 보드 추가
	// Controller
	@PostMapping("/delete")
	public ModelAndView deleteBoard(@ModelAttribute SearchHelper searchHelper) {
		ModelAndView mav = new ModelAndView("/board/list?cate=" + searchHelper.getCate() + "&pageNumber=1");

		// 게시글 삭제 메서드 실행
		boardService.deleteBoard(searchHelper.getBodIdx());

		mav.addObject("cate", searchHelper);
		return mav;
	}

	// 뎃글 기능
	@PostMapping("/commentWrite")
	public ModelAndView writeComment(@ModelAttribute CommentVO comment, @RequestParam("cate") int cate,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO vo = (MemberVO) session.getAttribute("userInfo");

		// 로그인 사용자의 아이디를 comment에 수동으로 설정
		if (vo != null) {
			comment.setUserId(vo.getUserId()); // 💡 이거 꼭 필요!
		}

		mav.addObject("userInfo", vo != null ? vo : new MemberVO());
		mav.addObject("comment", comment);

		boardService.writeComment(comment);
		mav.setViewName("redirect:/board/view?bodIdx=" + comment.getBodIdx() + "&pageNumber=1&cate=" + cate);

		return mav;
	}

	@PostMapping("/commentDelete")
	public ModelAndView commentDelete(@ModelAttribute CommentVO comment, @RequestParam("cate") int cate,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO vo = (MemberVO) session.getAttribute("userInfo");

		// 로그인 사용자의 아이디를 comment에 수동으로 설정
		if (vo != null) {
			comment.setUserId(vo.getUserId()); // 💡 이거 꼭 필요!
		}

		mav.addObject("userInfo", vo != null ? vo : new MemberVO());
		mav.addObject("comment", comment);

		boardService.deleteComment(comment.getComIdx());

		mav.setViewName("redirect:/board/view?bodIdx=" + comment.getBodIdx() + "&pageNumber=1&cate=" + cate);
		return mav;
	}

	// ✅ Controller
	@PostMapping("/like")
	@ResponseBody
	public ResponseEntity<?> like(@RequestBody HashMap<String, Object> requestMap) {
		log.info(requestMap.toString());
		Long bodIdx = Long.parseLong((String) requestMap.get("bodIdx"));
		String userId = (String) requestMap.get("userId");

		int result = boardService.toggleLike(bodIdx, userId);

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result);

		log.info(resultMap.toString());

		return ResponseEntity.ok(resultMap); // ✅ 현재 좋아요 수 반환
	}
	
	// 좋아요순 5개 개시글 출력
	@GetMapping("/topLiked")
	public String topLikedBoard(Model model) {
	    List<BoardDefaultVO> topList = boardService.getTop5LikedBoards();
	    model.addAttribute("topList", topList);
	    return "board/topLiked"; // 보여줄 Thymeleaf 페이지
	}

}
