package com.trip.webpage.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trip.webpage.vo.FestivalDetailVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/festival")
@Slf4j
public class FestivalController {

	// 공공데이터 포털에서 발급받은 서비스 키 (URL 디코딩된 상태여야 함)
	private final String serviceKey = "dfkqgFHSVFefp3CpeXZ5psT9s6iWXzaWacsy9b/gDvuEJpzyy9Vpah1z2ktIKXO85kIcRQAGZwdqUjUDbD6dkg==";

	// 축제 상세 페이지 요청 시 호출되는 메서드
	@GetMapping("/detail/{contentId}")
	public ModelAndView showFestivalDetail(@PathVariable String contentId) {
		// API 호출 URL 구성 (JSON 반환 + 개요 포함)
		String apiUrl = "https://apis.data.go.kr/B551011/KorService2/detailCommon2" + "?serviceKey=" + serviceKey
				+ "&MobileOS=ETC" + "&MobileApp=AppTest" + "&_type=json" + "&contentId=" + contentId + "&numOfRows=10"
				+ "&pageNo=1";

		log.info("요청 URL : {}", apiUrl);

		// RestTemplate 사용하여 API 호출
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
		String res = responseEntity.getBody();
		log.info("응답 바디 : {}", res);

		ModelAndView mav = new ModelAndView("/festival/festivalDetail");

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode itemNode = objectMapper.readTree(res).path("response").path("body").path("items").path("item")
					.get(0); // 단일 객체 추출

			Gson gson = new Gson();
			FestivalDetailVO festival = gson.fromJson(itemNode.toString(), FestivalDetailVO.class);
			log.info("test : {}", festival.toString());

			mav.addObject("festival", festival);
		} catch (Exception e) {
			log.error("JSON 파싱 실패", e);
			mav.addObject("festival", null);
		}

		return mav;
	}

	// 이런축제어때 랜덤축제추천
	@GetMapping("/recommend")
	public ModelAndView showRecommendedFestivals() {
		String apiUrl = "https://apis.data.go.kr/B551011/KorService2/searchKeyword2?serviceKey=" + serviceKey
				+ "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&arrange=C&keyword=축제";

		log.info("요청 URL : {}", apiUrl);

		// RestTemplate 사용하여 API 호출
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
		String res = responseEntity.getBody();

		List<FestivalDetailVO> recommended = new ArrayList<>();
		
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode itemsNode = objectMapper.readTree(res)
	                .path("response").path("body").path("items").path("item");

	        if (itemsNode.isArray()) {
	            List<FestivalDetailVO> all = new ArrayList<>();
	            for (JsonNode item : itemsNode) {
	                FestivalDetailVO vo = objectMapper.treeToValue(item, FestivalDetailVO.class);
	                if (vo.getFirstimage() != null && !vo.getFirstimage().isEmpty()) {
	                    all.add(vo);
	                }
	            }
	            Collections.shuffle(all); // 무작위 섞기
	            recommended = all.stream().limit(5).toList();
	        }
	        log.info(recommended.toString());

	    } catch (Exception e) {
	        log.error("추천 축제 JSON 파싱 실패", e);
	    }

	    ModelAndView mav = new ModelAndView("festival/recommend");
	    mav.addObject("recommendedList", recommended);
	    return mav;
	}
	
}
