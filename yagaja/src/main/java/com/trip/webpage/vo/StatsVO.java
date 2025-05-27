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
public class StatsVO {
	
	 private String day;
	 private int visitCount;
	 private int postCount;

}
