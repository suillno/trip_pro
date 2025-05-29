package com.trip.webpage.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.CommentVO;
import com.trip.webpage.vo.MemberVO;
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

	// 좋아요 중복체크 확인
	boolean existsLike(Long bodIdx, String userId);

	// 좋아요
	void addLike(Long bodIdx, String userId);

	// 좋아요 취소
	void removeLike(Long bodIdx, String userId);

	// 좋아요 카운트
	void updateLikeCount(Long bodIdx, int amount);

	// 좋아요 카운트
	int getLikeCount(Long bodIdx);

	// 좋아요순 상위 5개
	List<BoardDefaultVO> selectTop5LikedBoards();

	// 신고 여부 확인 05-29 조윤호
	int checkReport(Long bodIdx, String userId);

	// 신고 등록 05-29 조윤호
	void insertReport(Long bodIdx, String userId);

	// 신고 조회 05-29
	int getReportCount(Long bodIdx);

	// 신고 중복체크 확인
	boolean existsReport(Long bodIdx, String userId);

	// 신고 취소
	void removeReport(Long bodIdx, String userId);

	void addReport(Long bodIdx, String userId);
	
	//신고 3개이상 게시글 확인
	List<BoardDefaultVO> selectReportedBoards();
	
	// 신고 게시글 블라인드 처리
	int blockBoard(BoardDefaultVO defaultVO);

}
