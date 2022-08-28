package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBlockRes {
    private int userIdx; // 차단을 한 유저ID
    private int storeIdx; // 차단된 상점ID

}
