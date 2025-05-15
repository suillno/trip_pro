package com.trip.webpage.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.trip.webpage.vo.MemberVO;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

	// 전체 파일에 MemberVO userInfo 자동주입
    @ModelAttribute("userInfo")
    public MemberVO addUserInfo(HttpSession session) {
        Object user = session.getAttribute("userInfo");
        return user != null ? (MemberVO) user : null;
    }
}
