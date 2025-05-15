package com.trip.webpage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trip.webpage.vo.MemberVO;


@Controller // url을 호출했을때 응답을 처리 (어노테이션)
public class CommonController {

	/**
	 * 요청한 url이 어떤 메서드와 연결되는 것인지 처리
	 * @RequestMapping("/")
	 * @RequestMapping(value = "/")
	 * @RequestMapping(value = "/", method=RequestMethod.GET)
	 * @return
	 */
	// "/" 경로로 들어오는 GET 요청을 처리할 메서드에 매핑
	// 요청이 들어오면 "index" 뷰를 반환하고, 모델(Model)에 데이터도 함께 전달하는 메서드
	@GetMapping("/")
	public String index() {
	    return "index";  // 뷰 이름만 리턴해도 OK
	}

}


