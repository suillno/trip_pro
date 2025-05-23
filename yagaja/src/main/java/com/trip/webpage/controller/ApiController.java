package com.trip.webpage.controller;

import org.codehaus.groovy.classgen.ReturnAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.webpage.service.MemberService;

@RestController
@RequestMapping("/api")
public class ApiController {

	
	@Autowired
	private MemberService memberService;
	
	
	@GetMapping("/member/checkId")
	public ResponseEntity<?> userCheckId(
			@RequestParam("userId") String userId){
		return ResponseEntity.ok(memberService.selectMember());	
	}
	
}
