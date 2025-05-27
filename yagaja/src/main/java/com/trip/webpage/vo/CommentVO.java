package com.trip.webpage.vo;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {
    private Long idx;              // 댓글 고유번호 (PK)
    private String content;        // 댓글 내용
    private String regId;          // 등록자 ID
    private LocalDateTime regDate;// 등록일
    private String updId;          // 수정자 ID
    private LocalDateTime updDate;// 수정일
    private String userId;         // 사용자 ID (회원 참조)
    private Long bodIdx;           // 게시판 글번호 (FK)
    private String isVisible;      // 댓글 표시 여부 (Y/N)
}
