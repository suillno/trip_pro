package com.trip.webpage.service;

import java.util.List;
import java.util.Optional;

import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.CommentVO;
import com.trip.webpage.vo.SearchHelper;

public interface BoardService {

	BoardDefaultVO selectOne(Long bodIdx);

	List<BoardDefaultVO> selectList(SearchHelper searchHelper);

	int selectListCount(SearchHelper searchHelper);

	int insertBoard(BoardDefaultVO boardDefaultVO);

	BoardDefaultVO selectLatestByUserId(String id);

	// 2025-05-22 조윤호 타입 변경 void -> int
	int updateBoard(BoardDefaultVO boardDefaultVO);

	// 게시글 삭제
	void deleteBoard(Long bodIdx);

	public void updateUserCnt(Long bodIdx);

	// 댓글 저장
	void writeComment(CommentVO comment);

	// 댓글 리스트
	List<CommentVO> selectCommentList(Long bodIdx);

	void updateComment(Long comIdx);

	void deleteComment(Long comIdx);

	// 2025-05-28 조윤호
	// 좋아요가 눌러있는지 확인
	boolean existsLike(Long bodIdx, String userId);

	// 좋아요 토글
	int toggleLike(Long bodIdx, String userId);

	// 좋아요순 상위 5개
	public List<BoardDefaultVO> getTop5LikedBoards();

	// 신고버튼 토글 처리 05-29
	int toggleReport(Long bodIdx, String userId);
	
	// 신고 3개이상 게시글
	List<BoardDefaultVO> getReportedBoards();

	// 신고 게시글 블락처리
	int blockBoard(BoardDefaultVO defaultVO);
}
