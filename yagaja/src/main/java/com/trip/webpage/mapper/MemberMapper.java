package com.trip.webpage.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.vo.SearchHelper;
import com.trip.webpage.vo.LoginRequest;
import com.trip.webpage.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	List<MemberVO> selectMember();
	
	int selectListCount(SearchHelper searchHelper);
	
	int selectMemberCount();
	// MamberMapper.xml userLogin 쿼리를 자바 메서드형태로 선언
	MemberVO userLogin(LoginRequest loginRequest);
	
	List<MemberVO> selectList(SearchHelper searchHelper);
}
