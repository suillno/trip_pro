package com.trip.webpage.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trip.webpage.mapper.BoardMapper;
import com.trip.webpage.service.BoardService;
import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.CommentVO;
import com.trip.webpage.vo.SearchHelper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class BoardServiceImple implements BoardService {

	private BoardMapper boardMapper;

	@Override
	public List<BoardDefaultVO> selectList(SearchHelper searchHelper) {
		return boardMapper.selectList(searchHelper);
	}

	@Override
	public int selectListCount(SearchHelper searchHelper) {
		return boardMapper.selectListCount(searchHelper);
	}

	@Override
	public int insertBoard(BoardDefaultVO boardDefaultVO) {
		return boardMapper.insertBoard(boardDefaultVO);
	}

	@Override
	public BoardDefaultVO selectLatestByUserId(String id) {
		return boardMapper.selectLatestByUserId(id);
	}

	// 2025-05-26 조윤호 죄회수 조건문
	@Override
	public BoardDefaultVO selectOne(Long bodIdx) {
		// TODO Auto-generated method stub
		BoardDefaultVO vo = boardMapper.selectOne(bodIdx);
		if (vo != null)
			boardMapper.updateUserCnt(bodIdx);
		return vo;
	}

	@Override
	public int updateBoard(BoardDefaultVO boardDefaultVO) {
		return boardMapper.updateBoard(boardDefaultVO);
	}

	// 게시글 삭제
	@Override
	public void deleteBoard(Long bodIdx) {
		// TODO Auto-generated method stub
		boardMapper.deleteBoard(bodIdx);
	}

	// 2025-05-26 조윤호 updateUserCnt 추가
	public void updateUserCnt(Long bodIdx) {

	}
	
	// 댓글 저장
	@Override
    public void writeComment(CommentVO comment) {
		boardMapper.insertComment(comment);
    }

	// 댓글 리스트
    @Override
    public List<CommentVO> selectCommentList(Long bodIdx) {
        return boardMapper.selectCommentList(bodIdx);
    }

    // 댓글 수정
	@Override
	public void updateComment(Long comIdx) {
		boardMapper.updateComment(comIdx);
	}

	// 댓글 삭제
	@Override
	public void deleteComment(Long comIdx) {
		boardMapper.deleteComment(comIdx);
	}

    //용도: 특정 사용자가 해당 게시글에 이미 좋아요를 눌렀는지 확인  2025-05-28 조윤호
    @Override
    public boolean existsLike(Long bodIdx, String userId) {
        return boardMapper.existsLike(bodIdx, userId);
    }
   
    /**
     * 게시물의 좋아요 토글처리 (있으면 삭제, 없으면 삽입)
     * 그 후 좋아요 갯수 리턴
     */
    public int toggleLike(Long bodIdx, String userId) {
        boolean liked = boardMapper.existsLike(bodIdx, userId);
        if (liked) {
            boardMapper.removeLike(bodIdx, userId);
        } else {
            boardMapper.addLike(bodIdx, userId);
        }
        return boardMapper.getLikeCount(bodIdx);
    }
    
    // 좋아요순 상위 5개
    public List<BoardDefaultVO> getTop5LikedBoards() {
        return boardMapper.selectTop5LikedBoards();
    }
}
