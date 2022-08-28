package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyFeedRecommendRes {
    private int storeIdx; // 상점ID
    private String storeImg; // 상점이미지
    private String storeName; //상점명
    private int quantity; //상점의 상품갯수
    private int followersNum; // 상점의 팔로워 수
    private List<String> representProductsImg; // 상점의 대표상품 이미지 리스트
    private List<String> representProductsPrice; // 상점의 대표상품 가격 리스트
}
