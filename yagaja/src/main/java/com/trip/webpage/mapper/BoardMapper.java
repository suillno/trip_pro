package com.trip.webpage.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.CommentVO;
import com.trip.webpage.vo.SearchHelper;

@Mapper
public interface BoardMapper {

	// 2025-05-22 selectListCount,insertBoard,updateBoard 타입 변경 (int로)
	// updateBoard 와 deleteBoard 추가 아직 완성 아님 CSS먼저 진행예정

	BoardDefaultVO selectOne(Long bodIdx);

	List<BoardDefaultVO> selectList(SearchHelper searchHelper);

	int selectListCount(SearchHelper searchHelper);

	int insertBoard(BoardDefaultVO boardDefaultVO);

	BoardDefaultVO selectLatestByUserId(String id);

	int updateBoard(BoardDefaultVO boardDefaultVO);

	void deleteBoard(Long bodIdx);

	// 2025-05-26 조윤호 카운트문 추가
	void updateUserCnt(Long bodIdx);
	
	// 댓글 저장
	void insertComment(CommentVO comment);

	// 댓글 리스트
	List<CommentVO> selectCommentList(Long bodIdx);

	void updateComment(Long comIdx);

	void deleteComment(Long comIdx);
}
