package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyFeedFollowingRes {
    private int storeIdx; // 상점ID
    private String storeImg;// 팔로우한 상점의 이미지
    private String storeName; // 팔로우한 상점의 이름
    private int productsQuantity; // 팔로우한 상점이 가진 상품 수
    private int followers; // 팔로우한 상점의 팔로워 수
    private List<String> representProductsImg; // 팔로우한 상점의 상위 3개 상품 이미지
    private List<String> representProductsPrice; // 팔로우한 상점의 상위 3개 상품 가격
    private String status; // status
}
