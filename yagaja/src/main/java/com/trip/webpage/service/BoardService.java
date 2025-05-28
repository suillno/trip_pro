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
	
	//2025-05-22  조윤호 타입 변경 void -> int
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

}
