package com.trip.webpage.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberVO {
	
	private String userID;
	private Long isAdmin;
	private String userPw;
	private String userName;
	private String email;
	private String phoneNum;
	private Date regDate;
	private Date dropDate;
	private String userAddr;
	private char dropYn;
	private char emailAuth;
	private String emailAuthCode;
	private char blockCode;
	

}
