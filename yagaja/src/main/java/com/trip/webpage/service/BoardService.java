package com.trip.webpage.service;

import java.util.List;
import java.util.Optional;

import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.BoardVO;
import com.trip.webpage.vo.SearchHelper;

public interface BoardService {
	List<BoardVO> selectList(SearchHelper searchHelper);
	
	int selectListCount(SearchHelper searchHelper);
	
	void insertBoard(BoardDefaultVO boardDefaultVO); 
	
	Optional<BoardVO> selectBoard(Long boardNo);
	
	void updateBoard(BoardDefaultVO boardDefaultVO); 

	void deleteBoard(Long boardNo);
	
}
