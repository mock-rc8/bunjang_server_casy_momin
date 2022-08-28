package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchHeartReq {
    private int productIdx;
    public PatchHeartReq(){

    }
    public PatchHeartReq(int productIdx){
        this.productIdx = productIdx;
    }
}
