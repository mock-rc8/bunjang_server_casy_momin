package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyReviewListRes {
    private int reviewerID; // 리뷰를 쓴 상점ID(유저ID)
    private String reviewerName; // 리뷰를 쓴 상점명
    private String reviewerImg; // 리뷰를 쓴 상점 이미지
    private String created; // 리뷰를 쓴 시각부터 현재까지
    private int starCount; // 평점
    private String reviewText; // 리뷰내용
    private String productName; // 상품명
}
