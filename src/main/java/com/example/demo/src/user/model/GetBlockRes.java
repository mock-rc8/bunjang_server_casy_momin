package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBlockRes {
    private int storeIdx; // 차단한 상점ID
    private String storeImg; // 차단한 상점 이미지
    private String storeName; // 차단한 상점명
    private String created; // 차단한 시간
}
