package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyFeedInMyFeedRes {
    private int storeIdx; // 상점ID
    private int productsIdx; // 상품ID
    private String productsImg; // 상품 이미지
    private int price; // 상품 가격
    private String productsName; // 상품명
    private String storeImg; // 상점 이미지
    private String storeName; // 상점명
}
