package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFollwersListRes {
    private int storeIdx; // 나를 팔로우하는 상점ID(유저ID)
    private String storeImg; // 상점이미지
    private String storeName; // 상점명
    private int productsNum; // 해당 상점의 상품 수
    private int followersNum; // 해당 상점의 팔로워 수
}
