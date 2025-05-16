// 서비스 구현체 클래스: MemberService 인터페이스를 구현
package com.trip.webpage.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.vo.SearchHelper;
import com.trip.webpage.mapper.MemberMapper;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.LoginRequest;
import com.trip.webpage.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;

// @Service 어노테이션을 통해 Spring이 이 클래스를 서비스 빈으로 등록함
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	// MemberMapper(DAO)를 주입 받아 DB와 연결된 기능을 사용
	@Autowired
	private MemberMapper memberMapper;

	// 인터페이스에서 정의한 selectMember() 메서드 구현
	// → DAO를 통해 DB에서 회원 목록을 가져와 반환
	@Override
	public List<MemberVO> selectMember() {
		log.info(String.valueOf(memberMapper.selectMemberCount()));
		return memberMapper.selectMember(); // Mapper의 SQL 실행 결과 반환
	}

	// 로그인동작 구현 로그인결과 반환
	@Override
	public MemberVO userLogin(LoginRequest loginRequest) {
		MemberVO result = memberMapper.userLogin(loginRequest);
		if (result != null) {
            log.info("로그인 성공: {}", result.getUserId());
        } else {
            log.warn("로그인 실패: {}", loginRequest.getUserId());
        }
        return result;
    }

	@Override
	public List<MemberVO> selectList(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return memberMapper.selectMember();
	}

	@Override
	public int selectListCount(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return memberMapper.selectListCount(searchHelper);
	}
	
}
