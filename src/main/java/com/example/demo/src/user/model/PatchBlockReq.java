package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchBlockReq {
    private int storeIdx; // 차단을 취소할 상점ID
    public PatchBlockReq(){

    }
    public PatchBlockReq(int storeIdx){
        this.storeIdx=storeIdx;
    }
}
