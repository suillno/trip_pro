package com.trip.webpage.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trip.webpage.mapper.BoardMapper;
import com.trip.webpage.service.BoardService;
import com.trip.webpage.vo.BoardDefaultVO;
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

	@Override
	public BoardDefaultVO selectOne(Long bodIdx) {
		// TODO Auto-generated method stub
		
		
		
		return boardMapper.selectOne(bodIdx);
	}

	@Override
	public int updateBoard(BoardDefaultVO boardDefaultVO) {
		return boardMapper.updateBoard(boardDefaultVO);
	}

	@Override
	public void deleteBoard(Long boardNo) {
		// TODO Auto-generated method stub
		boardMapper.deleteBoard(boardNo);
	}

	
	
	
}
