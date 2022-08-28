package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTransactionSalesRes {
    private int productsIdx; // 내가 판매한 상품ID
    private String productsImg; // 내가 판매한 상품 이미지
    private String productsName; // 내가 판매한 상품명
    private int storeIdx; // 상품을 산 상점ID
    private String storeImg; // 상품을 산 상점 이미지
    private String storeName; // 상품을 산 상점명
    private String created; // 거래한 시간
}
