package com.trip.webpage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.webpage.mapper.MemberMapper;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper memberMapper;

	@Override
	public List<MemberVO> seletMember() {
		return memberMapper.selectMember();
	}
	
}
