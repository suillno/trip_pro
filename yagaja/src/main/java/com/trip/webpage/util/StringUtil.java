package com.trip.webpage.util;

import com.trip.webpage.vo.SearchHelper;

public class StringUtil {


	/**
	 * URL과 파라미터를 합친 String 반환
	 * @param url
	 * @param searchHelper
	 * @return
	 */
	public static String searchString(
			String url, 
			SearchHelper searchHelper) {
		StringBuilder str = new StringBuilder();
		// /board/view
		str.append(url);
		str.append("?pageNumber=" + searchHelper.getPageNumber());
		str.append("&pageSize=" + searchHelper.getPageSize());
		str.append("&searchType=" + (searchHelper.getSearchType() != null ? searchHelper.getSearchType() : ""));
		str.append("&searchKeyword=" + (searchHelper.getSearchKeyword() != null ? searchHelper.getSearchKeyword() : ""));
		return str.toString();
		
	}
	
	
	
	
}