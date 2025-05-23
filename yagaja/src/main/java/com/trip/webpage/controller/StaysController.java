package com.trip.webpage.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.webpage.vo.FestivalDetailVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/stays")
@Slf4j
public class StaysController {

	// 공공데이터 포털에서 발급받은 서비스 키 (URL 디코딩된 상태여야 함)
	private final String serviceKey = "dfkqgFHSVFefp3CpeXZ5psT9s6iWXzaWacsy9b/gDvuEJpzyy9Vpah1z2ktIKXO85kIcRQAGZwdqUjUDbD6dkg==";

	// 숙박지 랜덤 추천
	@GetMapping("/stayRecommend")
	public ModelAndView showRecommendedStays() {
		String apiUrl = "https://apis.data.go.kr/B551011/KorService2/searchKeyword2?serviceKey=" + serviceKey
				+ "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&arrange=C&keyword=숙박";

		log.info("요청 URL : {}", apiUrl);

		// RestTemplate 사용하여 API 호출
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
		String res = responseEntity.getBody();

		List<FestivalDetailVO> recommended = new ArrayList<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode itemsNode = objectMapper.readTree(res).path("response").path("body").path("items").path("item");

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
			log.error("추천 숙박 JSON 파싱 실패", e);
		}

		ModelAndView mav = new ModelAndView("stays/stayRecommend");
		mav.addObject("recommendedList", recommended);
		return mav;
	}

}
