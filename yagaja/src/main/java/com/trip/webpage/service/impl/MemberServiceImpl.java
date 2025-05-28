// 서비스 구현체 클래스: MemberService 인터페이스를 구현
package com.trip.webpage.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.webpage.vo.SearchHelper;
import com.trip.webpage.vo.StatsVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.trip.webpage.mapper.BoardMapper;
import com.trip.webpage.mapper.MemberMapper;
import com.trip.webpage.service.MemberService;
import com.trip.webpage.vo.BoardDefaultVO;
import com.trip.webpage.vo.LoginRequest;
import com.trip.webpage.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.SecureRandom;

// @Service 어노테이션을 통해 Spring이 이 클래스를 서비스 빈으로 등록함
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	// MemberMapper(DAO)를 주입 받아 DB와 연결된 기능을 사용
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private BoardMapper boardMapper;

	// 05-28 추가
	@Autowired
	private JavaMailSender mailSender;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

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
	public int selectMemberCount(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return memberMapper.selectMemberCount(searchHelper);
	}

	@Override
	public int blockMember(MemberVO memberVO) {
		// TODO Auto-generated method stub
		return memberMapper.blockMember(memberVO);
	}

	@Override
	public MemberVO findUserId(MemberVO memberVO) {
		MemberVO result = memberMapper.findUserId(memberVO);

		return result;
	}

	@Override
	public List<MemberVO> selectList(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return memberMapper.selectMember(searchHelper);
	}

	@Override
	public int selectListCount(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return memberMapper.selectListCount(searchHelper);
	}

	@Override
	public MemberVO findById(String id) {
		// TODO Auto-generated method stub
		log.info(id);
		return memberMapper.findById(id);
	}

	// 회원 조회
	@Override
	public List<MemberVO> selectMemberList(SearchHelper searchHelper) {
		// TODO Auto-generated method stub
		return memberMapper.selectMemberList(searchHelper);
	}

	// 회원가입
	@Override
	public int insertUser(MemberVO memberVO) {
		// TODO Auto-generated method stub

		// 이메일 인증코드 작성
		String emailAuthCode = UUID.randomUUID().toString();
		log.info(emailAuthCode);
		memberVO.setEmailAuthCode(emailAuthCode);

		return memberMapper.insertUser(memberVO);
	}

	// 비밀번호 변경 05-22
	@Override
	public void updatePassword(MemberVO memberVO) {
		// TODO Auto-generated method stub
		memberMapper.updatePassword(memberVO);

	}

	@Override
	public BoardDefaultVO selectLatestByUserId(String id) {
		// TODO Auto-generated method stub
		return boardMapper.selectLatestByUserId(id);
	}

	// 회원 탈퇴 05-23
	@Override
	public void updateShip(MemberVO memberVO) {
		// TODO Auto-generated method stub
		memberMapper.updateShip(memberVO);
	}

	// 회원탈퇴 비밀번호 검증 05-23
	@Override
	public boolean checkPasswordForWithdraw(String userPw, HttpServletRequest request) {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserPw(userPw);
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("userInfo");
		loginRequest.setUserId(memberVO.getUserId());

		MemberVO result = memberMapper.userLogin(loginRequest);

		if (result != null) {
			memberMapper.updateShip(memberVO);
			return true;
		}
		return false;
	}

	@Override
	public void updateAdminRole(MemberVO vo) {
		memberMapper.updateAdminRole(vo);

	}

	// 웹페이지 방문자 저장
	@Override
	public void saveVisit(String userId) {
		int alreadyVisited = memberMapper.checkVisitToday(userId);
		if (alreadyVisited == 0) {
			memberMapper.insertVisit(userId);
		}
	}

	@Override
	public List<StatsVO> getDailyStats() {
		return memberMapper.selectVisitAndPostStats();
	}

	// 05-28 추가
	@Override
	public void sendTemporaryPassword(MemberVO memberVO) {
		// 1) 이메일로 회원 조회
		MemberVO member = memberMapper.selectByEmail(memberVO);
		if (member == null) {
			throw new IllegalArgumentException("등록된 사용자가 없습니다.");
		}

		// 2) 랜덤 비밀번호 생성
		String tempPassword = generateRandomPassword(10);

		// 3) 암호화 후 DB에 저장
//		    String encryptedPw = passwordEncoder.encode(tempPassword);
//		    member.setUserPw(encryptedPw);   // 필드명은 MemberVO에서 비밀번호 필드명 확인
//		    memberMapper.updatePassword(member);

		// 4) 이메일 전송
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(memberVO.getEmail()); // 0530분 수정
		message.setSubject("[Yagaja] 임시 비밀번호 안내");
		message.setText("임시 비밀번호는 아래와 같습니다:\n\n" + tempPassword + "\n\n로그인 후 비밀번호를 꼭 변경해주세요.");
		mailSender.send(message);
	}

	// 랜덤 비밀번호 생성 메서드
	private String generateRandomPassword(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	@Override // 0530분 수정
	public MemberVO findByEmail(MemberVO memberVO) {
		// TODO Auto-generated method stub
		return memberMapper.selectByEmail(memberVO);
	}

	@Override
	public void sendTempPassword(String email, String tempPassword) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("[Yagaja] 임시 비밀번호 안내");
		message.setText("임시 비밀번호는 다음과 같습니다: " + tempPassword + "\n로그인 후 반드시 비밀번호를 변경해 주세요.");

		mailSender.send(message);
	}

}
