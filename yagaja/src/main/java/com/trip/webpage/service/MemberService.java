package com.trip.webpage.service;

import java.util.List;
import java.util.Optional;

import com.trip.webpage.vo.SearchHelper;
import com.trip.webpage.vo.LoginRequest;
import com.trip.webpage.vo.MemberVO;

public interface MemberService {
	// MemberVO 목록을 조회하는 메서드 선언
	// 실제 구현은 MemberServiceImpl에서 수행함
	// DAO (MemberMapper)의 selectMember()를 호출하여 DB에서 회원 정보를 가져옴
	MemberVO userLogin(LoginRequest loginRequest); // Mapper 로그인용 메서드호출
	int selectMemberCount(SearchHelper searchHelper);
	int blockMember(MemberVO memberVO);
	int insertUser(MemberVO memberVO);
	MemberVO findUserId(MemberVO memberVO); 
	List<MemberVO> selectList(SearchHelper searchHelper);
	int selectListCount(SearchHelper searchHelper);
	MemberVO findById(String id);
	// 회원 조회
	List<MemberVO> selectMember();
	// 회원 가입
	List<MemberVO> selectMemberList(SearchHelper searchHelper);
	// 비밀번호 변경 05-22
	void  updatePassword(MemberVO memberVO);
	
}
