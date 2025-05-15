package com.trip.webpage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig 클래스는 Spring MVC의 설정을 담당합니다.
 * - 인터셉터, 리소스 핸들러, 포맷터 등을 설정할 수 있습니다.
 * 
 * @Configuration 어노테이션을 통해 이 클래스는 스프링 설정 클래스임을 선언합니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Bean 설명
     * - Bean은 Spring 컨테이너가 생성하고 관리하는 객체입니다.
     * - 생성자 주입을 통해 SessionInterceptor를 주입받습니다.
     * - DI(의존성 주입)를 통해 클래스 간 결합도를 낮추고 유지보수성을 향상시킵니다.
     */
    private final SessionInterceptor sessionInterceptor;

    /**
     * 생성자 주입 방식으로 인터셉터 객체를 주입
     * @param sessionInterceptor 세션 검사용 인터셉터
     */
    public WebConfig(SessionInterceptor sessionInterceptor) {
        this.sessionInterceptor = sessionInterceptor;
    }

    /**
     * 인터셉터 등록 메서드
     * - 특정 URL 패턴에 대해 SessionInterceptor가 동작하도록 설정합니다.
     * - 예: "/admin/**" 경로나 "/member/info" 요청 시 세션 검사를 수행합니다.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor 등록
        registry.addInterceptor(sessionInterceptor)
                // /admin 하위 모든 경로에 적용
                .addPathPatterns("/admin/**")
                // /member/info 경로에도 적용
                .addPathPatterns("/member/info");
        // 기본 구현을 호출 (생략 가능하지만 명시적 호출)
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
