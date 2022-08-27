package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHeartRes {
    private String productsImg; // 상품 이미지
    private String productsName;//상품명
    private int price;//상품가격
    private String storeName;//상점명
    private String timeGap;//상품을 올린지 얼마나 시간이 지났는지 ex) 21시간 전
}
