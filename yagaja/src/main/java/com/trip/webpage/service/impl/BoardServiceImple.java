package com.trip.webpage.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trip.webpage.mapper.BoardMapper;
import com.trip.webpage.service.BoardService;
import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.BoardVO;
import com.trip.webpage.vo.SearchHelper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class BoardServiceImple implements BoardService {
	
	private BoardMapper boardMapper;

	@Override
	public List<BoardVO> selectList(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return boardMapper.selectList(searchHelper);
	}

	@Override
	public int selectListCount(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return boardMapper.selectListCount(searchHelper);
	}

	public void insertBoard(BoardVO boardVO) {
		// TODO Auto-generated method stub
		boardMapper.insertBoard(boardVO);
	}

	@Override
	public Optional<BoardVO> selectBoard(Long boardNo) {
		// TODO Auto-generated method stub
		return boardMapper.selectBoard(boardNo);
	}

	public void updateBoard(BoardVO boardVO) {
		// TODO Auto-generated method stub
		boardMapper.updateBoard(boardVO);
	}

	@Override
	public void deleteBoard(Long boardNo) {
		// TODO Auto-generated method stub
		boardMapper.deleteBoard(boardNo);
	}

	@Override
	public void insertBoard(BoardDefaultVO boardDefaultVO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBoard(BoardDefaultVO boardDefaultVO) {
		// TODO Auto-generated method stub
		
	}

}
