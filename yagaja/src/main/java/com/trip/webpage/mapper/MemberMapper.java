package com.trip.webpage.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.trip.webpage.vo.SearchHelper;
import com.trip.webpage.vo.StatsVO;
import com.trip.webpage.vo.LoginRequest;
import com.trip.webpage.vo.MemberVO;

@Mapper
public interface MemberMapper {

	List<MemberVO> selectMember(SearchHelper searchHelper);

	List<MemberVO> selectMember();

	int selectMemberCount(SearchHelper searchHelper);

	int selectMemberCount();

	// MamberMapper.xml userLogin 쿼리를 자바 메서드형태로 선언
	MemberVO userLogin(LoginRequest loginRequest);

	int blockMember(MemberVO memberVO);

	int insertUser(MemberVO memberVO);

	MemberVO findUserId(MemberVO memberVO);

	List<MemberVO> selectList(SearchHelper searchHelper);

	MemberVO findById(String id);

	int selectListCount(SearchHelper searchHelper);

	List<MemberVO> selectMemberList(SearchHelper searchHelper);

	// 비밀번호 변경
	void updatePassword(MemberVO memberVO);

	// 회원탈퇴
	void updateShip(MemberVO memberVO);

	// 관리자 계정관리
	void updateAdminRole(MemberVO vo);
	
	// 웸페이지 방문자저장 (중복확인)
	int checkVisitToday(String userId);

	// 웹페이지 방문자저장
	void insertVisit(String userId);
	
	// 통계용
	List<StatsVO> selectVisitAndPostStats(); 
}
