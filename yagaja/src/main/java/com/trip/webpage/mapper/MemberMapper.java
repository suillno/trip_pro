package com.trip.webpage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.trip.webpage.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	List<MemberVO> selectMember();
	
	int selectMemberCount();
	
}
