package com.trip.webpage.vo;

import java.sql.Date;

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
public class BoardVO {
	private Long rowNumber;
	private Long boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriter;
	private Date boardRegDate;
	
	
	
	
	
	
	
	
}

