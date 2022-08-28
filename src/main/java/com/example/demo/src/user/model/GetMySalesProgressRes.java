package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMySalesProgressRes {
    private int productsIdx; // 상품ID
    private String productsImg; // 상품이미지
    private String productsName; // 상품명
    private int price; // 상품 가격
    private String created; // 상품 기재 시간
    private int pay; // 번개페이 사용여부
    private String saleStatus; //판매상태
}
