package com.trip.webpage.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.trip.webpage.YagajaApplication;
import com.trip.webpage.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 세션 기반 로그인 검사용 인터셉터 클래스
 * - 특정 요청 전에 세션을 검사하고, 로그인 여부를 판단해 접근을 제어함
 */
@Component
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {

    // 현재는 사용되지 않는 의존성 (필요 없으면 제거 가능)
    private final YagajaApplication application;

    // 생성자 주입 방식
    SessionInterceptor(YagajaApplication application) {
        this.application = application;
    }

    /**
     * 컨트롤러 요청 전에 실행되는 메서드
     * - 로그인 여부를 세션으로 검사
     * - 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("세션 인터셉터 실행");

        // 1. 기존 세션 가져오기 (없으면 null 반환)
        HttpSession session = request.getSession(false);

        // 2. 세션이 존재하고 로그인 정보(userInfo)가 있으면 통과
        if (session != null) {
            MemberVO userInfo = (MemberVO) session.getAttribute("userInfo");
            if (userInfo != null) {
                return true; // 요청 계속 진행 (컨트롤러로 이동)
            }
        }

        // 3. 세션이 없거나 로그인 정보가 없을 경우 → 로그인 페이지로 리다이렉트
        String requestUrl = request.getRequestURI(); // 사용자가 원래 접근하려던 주소
        session = request.getSession(); // 새로운 세션 생성
        session.setAttribute("redirectUrl", requestUrl); // 로그인 후 되돌아갈 주소 저장

        // 4. 로그인 페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/member/login");

        return false; // 컨트롤러로 요청 전달 차단
    }
}
