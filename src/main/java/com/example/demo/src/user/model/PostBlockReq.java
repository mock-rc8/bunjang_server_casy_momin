package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostBlockReq {
    private int storeIdx; // 차단할 상점 ID
    public PostBlockReq(){

    }
    public PostBlockReq(int storeIdx){
        this.storeIdx = storeIdx;
    }
}
