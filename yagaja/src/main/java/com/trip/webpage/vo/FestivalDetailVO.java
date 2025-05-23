package com.trip.webpage.vo;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FestivalDetailVO {

    @JsonProperty("contentid")
    private String contentid;

    @JsonProperty("title")
    private String title;

    @JsonProperty("createdtime")
    private String createdtime;

    @JsonProperty("modifiedtime")
    private String modifiedtime;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("telname")
    private String telname;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("firstimage")
    private String firstimage;

    @JsonProperty("addr1")
    private String addr1;

    @JsonProperty("addr2")
    private String addr2;

    @JsonProperty("mapx")
    private String mapx;

    @JsonProperty("mapy")
    private String mapy;

    @JsonProperty("overview")
    private String overview;
}
