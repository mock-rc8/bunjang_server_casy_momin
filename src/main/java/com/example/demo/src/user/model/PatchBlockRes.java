package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchBlockRes {
    private int storeIdx; // 차단취소한 상점ID
    private String status; // 차단취소한 상점의 status
}
