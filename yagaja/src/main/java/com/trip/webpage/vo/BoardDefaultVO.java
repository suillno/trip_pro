package com.trip.webpage.vo;

import java.security.Timestamp;
import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDefaultVO {

	private Long bodIdx;
	private Integer cate;
	private String bodTitle;
	private String bodConstent;
	private String regId;
	private LocalDateTime reg2Date;
	private String updateId;
	private LocalDateTime updateDate;
	private String fileMstId;
	private String userId;
	private String isVisible;
	private int userCnt;
	private String fesTitle;
	private String accTitle;
	private String report;
	private String userName;
	private int likeCnt;
	// 05-29 조윤호
	private int reportCnt;

}
